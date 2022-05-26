package com.tdonuk.scraper.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdonuk.scraper.model.BookCard;
import com.tdonuk.scraper.model.BookSource;
import com.tdonuk.scraper.model.entity.Book;
import com.tdonuk.scraper.util.JsonValidator;
import com.tdonuk.scraper.util.NumericUtils;
import com.tdonuk.scraper.util.UserAgentGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public abstract class ScraperAdapter implements BookScraper{
	private String searchQuery = "";
	private String baseUrl = "";
	private String searchUrl = "";
	
	private Random random = new Random();
	
	private Document currentPage;
	
	private String userAgent = UserAgentGenerator.getRandomUserAgent();
	
	private synchronized void updateUserAgent() {
		userAgent = UserAgentGenerator.getRandomUserAgent();
	}
	
	
	public ScraperAdapter(Document doc) {
		this.currentPage = doc;

		new Thread(() -> {
			try {
				while(1 == 1) {
					updateUserAgent();
					Thread.sleep(1000 * 60 * 10);
				}
			} catch(Exception e) {
				
			}
			
		}).start();
	}
	
	protected String userAgent() {
		return this.userAgent;
	}
	
	public ScraperAdapter( ) {}
	
	protected Random random() {
		return this.random;
	}

	protected String getBaseUrl() {
		return baseUrl;
	}

	protected void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	protected Document getCurrentPage() {
		return currentPage;
	}

	protected void setCurrentPage(Document currentPage) {
		this.currentPage = currentPage;
	}
	
	protected abstract String baseUrl();
	protected abstract String searchPath(String key);
	protected abstract boolean isValid(String url);
	
	protected void validateUrls(List<String> urls) {
		for(int i = 0; i < urls.size(); i++) {
			String u = urls.get(i);
			
			if(!u.startsWith(baseUrl())) {
				u = baseUrl() + (u.startsWith("/") ? u : "/" + u);
				
				urls.set(i, u);
			}
		}
	}
	
	public Set<Book> parse(Set<String> urls) throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
		Set<Book> books = new HashSet<>();
		Book book;
		int index = 0;
		for(String url : urls) {
			index++;
			
			if(!isValid(url)) { // if it's not valid
				continue;
			}
			
			try {
				setCurrentPage(Jsoup.connect(url).userAgent(userAgent()).get());
				
				String jsonData = getCurrentPage().selectFirst("script[type='application/ld+json']").data().replaceAll("\r", "").replaceAll("\n", "").trim();
				
				jsonData = JsonValidator.validate(jsonData);
				
				JsonNode node = jsonMapper.readTree(jsonData);
				
				book = handleJson(node);
				
				if(null != book) books.add(book);
				
				log("parsing status: ["+index+"/"+ urls.size() +"] [success: "+books.size()+", fails: "+(urls.size() - books.size())+"]");
				
				Thread.sleep(random().nextInt(30));
			} catch(Exception e) {
				err("error while parsing: " + e.getMessage());
				continue;
			}
		}
		
		return books;
	}
	
	protected abstract Elements selectPagination(Document doc);
	
	protected abstract Set<BookCard> selectProducts(Document doc);
	
	protected abstract Document getPage(String key, int num) throws IOException;
	
	public final Set<BookCard> search(String key) throws Exception{
		String url = searchPath(key);
		
		Document doc = Jsoup.connect(url).userAgent(userAgent()).get();
		if(doc != null) {
			log("starting search");
			
			Set<BookCard> items = selectProducts(doc);
			
			log("found elements: " + items.size());
			
			return items;
		} else {
			err("can not connect to search page. url: " + url);
		}
		
		return Set.of();
	}
	
	protected void log(String log) {
		System.out.println(getSource() + "\t" + log);
	}
	
	protected void err(String err) {
		System.err.println(getSource() + "\t" + err);
	}
	
	protected abstract Book handleJson(JsonNode node) throws Exception;
	
	public abstract BookSource getSource();
}
