package com.tdonuk.scraper.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tdonuk.scraper.model.entity.Book;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SearchResult {
	private BookSource source;
	private Set<BookCard> result;
	double averagePrice;
	double highestPrice;
	double lowestPrice;
	
	public SearchResult(BookSource source, Set<BookCard> result) {
		this.source = source;
		this.result = result;
	}
}
