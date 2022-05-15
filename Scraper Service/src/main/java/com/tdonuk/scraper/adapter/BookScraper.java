package com.tdonuk.scraper.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.tdonuk.scraper.model.entity.Book;

public interface BookScraper {
	void parseTitle(Book book) throws Exception;
	void parseIsbn(Book book) throws Exception;
	void parseDescription(Book book) throws Exception;
	void parsePrice(Book book) throws Exception;
	void parseRating(Book book) throws Exception;
	void parseRateCount(Book book) throws Exception;
	void parseImageUrl(Book book) throws Exception;
	void parseBookUrl(Book book) throws Exception;
	void parseTags(Book book) throws Exception;
	void parseLanguage(Book book) throws Exception;
	void parsePageCount(Book book) throws Exception;
	void parsePublishDate(Book book) throws Exception;
	void parseAuthors(Book book) throws Exception;
	
	default Book parseDefault() throws Exception {
		Book book;
		
		book = new Book();
		
		parseTitle(book);
		parseIsbn(book);
		parseDescription(book);
		parsePrice(book);
		parseRateCount(book);
		parseRating(book);
		parseImageUrl(book);
		parseBookUrl(book);
		parseTags(book);
		parseLanguage(book);
		parseAuthors(book);
		parsePageCount(book);
		parsePublishDate(book);	
		
		return book;
	}
}
