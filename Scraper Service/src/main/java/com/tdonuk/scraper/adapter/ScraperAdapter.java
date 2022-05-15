package com.tdonuk.scraper.adapter;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;

import org.jsoup.nodes.Document;

import com.tdonuk.scraper.model.BookSource;
import com.tdonuk.scraper.model.entity.Book;
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
	
	public abstract List<Book> parse(Set<String> urls) throws Exception;
	
	public abstract Set<String> search(String key) throws Exception;
	
	public abstract BookSource getSource();
}
