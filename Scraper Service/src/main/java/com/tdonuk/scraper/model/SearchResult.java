package com.tdonuk.scraper.model;

import java.util.List;
import java.util.Map;

import com.tdonuk.scraper.model.entity.Book;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SearchResult {
	private String source;
	private List<Book> result;
	double averagePrice;
	double highestPrice;
	double lowestPrice;
	
	public SearchResult(String source, List<Book> result) {
		this.source = source;
		this.result = result;
		
		lowestPrice = result.stream().mapToDouble(book -> book.getPrice()).min().getAsDouble();
		highestPrice = result.stream().mapToDouble(book -> book.getPrice()).max().getAsDouble();
		averagePrice = result.stream().mapToDouble(book -> book.getPrice()).average().getAsDouble();
	}
}
