package com.tdonuk.scraper.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tdonuk.scraper.model.entity.Book;

import lombok.Data;

@Data
public class ResultWrapper {
	private List<SearchResult> resultSet;
	
	@JsonIgnore
	private List<Book> common; // 
	
	public ResultWrapper(List<SearchResult> resultSet) {
		this.resultSet = resultSet;
		
		process();
	}
	
	private void  process() {
		common = new ArrayList<>();
		
		for(SearchResult result : resultSet) {
			common.addAll(result.getResult());
		}
		
		for(Book book : common) {
			List<Book> alternatives = common.stream().filter(b -> b.getIsbn().equals(book.getIsbn())).collect(Collectors.toList());
			
			Collections.sort(alternatives, Comparator.comparing(Book::getPrice));
			
			Book b = alternatives.get(0); // lowest price
			b.setLowestPrice(true);
		}
	}
}
