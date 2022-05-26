package com.tdonuk.scraper.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.tdonuk.scraper.adapter.BookScraper;
import com.tdonuk.scraper.adapter.DRAdapter;
import com.tdonuk.scraper.adapter.IdefixAdapter;
import com.tdonuk.scraper.adapter.KitapyurduAdapter;
import com.tdonuk.scraper.adapter.ScraperAdapter;
import com.tdonuk.scraper.model.BookCard;
import com.tdonuk.scraper.model.BookSource;
import com.tdonuk.scraper.model.ResultWrapper;
import com.tdonuk.scraper.model.SearchResult;
import com.tdonuk.scraper.model.entity.*;

@Service
public class ScraperService {
	
	// when a new adapter is created, just add it here to get it working, no need to anything else.
	private List<ScraperAdapter> adapters = List.of(
			new DRAdapter(), new IdefixAdapter()
		);
	
	private Random random = new Random();
	
	// searches the given keyword through pre-determined websites
	public ResultWrapper searchDefault(final String key) throws Exception {
		Map<ScraperAdapter , Set<BookCard>> results = new HashMap<>();
		
		ExecutorService es = Executors.newFixedThreadPool(adapters.size());
		
		for(ScraperAdapter adapter : adapters) {
			if(adapter != null) {
				es.submit(new Runnable() {
					public void run() {
						try {
							results.put(adapter, adapter.search(key));
						} catch (Exception e) {
							System.out.println("Error while searching");
						}
					}
				});
			}
		}
		es.shutdown();
		es.awaitTermination(10, TimeUnit.SECONDS);
		
		List<SearchResult> result = new ArrayList<>();
		
		for(Entry<ScraperAdapter, Set<BookCard>> searchResult : results.entrySet()) {
			try {
				result.add(new SearchResult(searchResult.getKey().getSource(), searchResult.getValue()));
			} catch(Exception e) {
				System.err.println(e.getMessage());
				continue;
			}
		}
		
		return new ResultWrapper(result);
	}
	
	public Set<Book> parseBooks(Set<BookCard> products) throws Exception{		
		Set<Book> books = new HashSet<Book>();
		
		ExecutorService es = Executors.newFixedThreadPool(products.stream().map(card -> card.getSource().name()).collect(Collectors.toSet()).size());
		
		for(BookCard card : products) {
			es.submit(() -> {
				try {
					BookSource source = Enum.valueOf(BookSource.class, card.getSource().name());
					
					books.addAll(source.adapter().parse(products.stream().filter(b -> b.getSource() == source).map(b -> b.getUrl()).collect(Collectors.toSet())));
				} catch(Exception e) {
					System.err.println("Error while parsing: " + e.getMessage());
				}

			});
		}
		es.shutdown();
		es.awaitTermination(10, TimeUnit.SECONDS);
		
		return books;
	}
	
}
