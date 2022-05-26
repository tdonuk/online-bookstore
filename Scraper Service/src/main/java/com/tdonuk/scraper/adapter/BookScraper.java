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
		
		try {
			parseBookUrl(book);
		} catch (Exception e) {
			throw e;
		}
		try {
			parseTitle(book);
		} catch (Exception e) {
			
		}
		try {
			parseIsbn(book);
		} catch (Exception e) {

		}
		try {
			parseDescription(book);
		} catch (Exception e) {

		}
		try {
			parsePrice(book);
		} catch (Exception e) {

		}
		try {
			parseRateCount(book);
		} catch (Exception e) {

		}
		try {
			parseRating(book);
		} catch (Exception e) {

		}
		try {
			parseImageUrl(book);
		} catch (Exception e) {

		}
		try {
			parseTags(book);
		} catch (Exception e) {

		}
		try {
			parseLanguage(book);
		} catch (Exception e) {

		}
		try {
			parseAuthors(book);
		} catch (Exception e) {

		}
		try {
			parsePageCount(book);
		} catch (Exception e) {

		}
		try {
			parsePublishDate(book);
		} catch (Exception e) {

		}	
		
		return book;
	}
}
