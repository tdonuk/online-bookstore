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
import com.tdonuk.scraper.adapter.KitapyurduAdapter;
import com.tdonuk.scraper.adapter.ScraperAdapter;
import com.tdonuk.scraper.model.ResultWrapper;
import com.tdonuk.scraper.model.SearchResult;
import com.tdonuk.scraper.model.entity.*;

@Service
public class ScraperService {
	// when a new adapter is created, just add it here to get it working, no need to anything else.
	private List<ScraperAdapter> adapters = List.of(
			new KitapyurduAdapter()
			);
	
	private Random random = new Random();
	
	
	// searches the given keyword through pre-determined websites
	public ResultWrapper searchDefault(final String key) throws Exception {
		Map<ScraperAdapter , Set<String>> productUrls = new HashMap<>();
		
		ExecutorService ws = Executors.newFixedThreadPool(adapters.size());
		
		for(ScraperAdapter adapter : adapters) {
			if(adapter != null) {
				ws.submit(new Runnable() {
					public void run() {
						try {
							productUrls.put(adapter, adapter.search(key));
						} catch (Exception e) {
							System.out.println("Error");
						}
					}
				});
			}
		}
		ws.shutdown();
		ws.awaitTermination(10, TimeUnit.SECONDS);
		
		Map<String, Future<List<Book>>> futureList = new HashMap<>();
		
		ws = Executors.newFixedThreadPool(productUrls.size());
		
		for(Entry<ScraperAdapter, Set<String>> entry : productUrls.entrySet()) {
			Future<List<Book>> future = ws.submit(new Callable<List<Book>>() {

				@Override
				public List<Book> call() throws Exception {
					return entry.getKey().parse(entry.getValue());
				}
				
			});
			
			futureList.put(entry.getKey().getSource().text(), future);
		}
		
		ws.shutdown();
		ws.awaitTermination(20, TimeUnit.SECONDS);
		
		List<SearchResult> result = new ArrayList<>();
		
		for(Entry<String, Future<List<Book>>> future : futureList.entrySet()) {
			if(future.getValue().isDone()) {
				if(future.getValue().get() != null) {
					result.add(new SearchResult(future.getKey(), future.getValue().get()));
				}
			}
		}
		
		ResultWrapper wrapper = new ResultWrapper(result);

		return wrapper;
		
	}
	
}
