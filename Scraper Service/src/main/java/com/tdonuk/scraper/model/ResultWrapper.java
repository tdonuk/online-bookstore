package com.tdonuk.scraper.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.tdonuk.scraper.model.entity.Book;

import lombok.Data;

@Data
public class ResultWrapper {
	private List<SearchResult> resultSet;
	
	@JsonIgnore
	private List<BookCard> common;
	
	public ResultWrapper(List<SearchResult> resultSet) {
		this.resultSet = resultSet;
		
		process();
	}
	
	private void  process() {
		common = new ArrayList<>();
		
		for(SearchResult result : resultSet) { // add all results to one list
			common.addAll(result.getResult());
		}
		
		for(SearchResult result : resultSet) { // edit this list to contain only the common elements
			common.retainAll(result.getResult());
		}
		
		for(SearchResult result : resultSet) { // edit resultsets to remove uncommon elements
			result.getResult().retainAll(common);
		}
		
		for(SearchResult result : resultSet) {
			result.setLowestPrice(result.getResult().stream().mapToDouble(book -> book.getPrice()).min().orElse(0));
			result.setAveragePrice(result.getResult().stream().mapToDouble(book -> book.getPrice()).average().orElse(0));
			result.setHighestPrice(result.getResult().stream().mapToDouble(book -> book.getPrice()).max().orElse(0));
		}
		
		for(BookCard book : common) { // handle the price comparing
			
			List<BookCard> alternatives = common.stream().filter(b -> b.equals(book)).collect(Collectors.toList());
			
			Collections.sort(alternatives, Comparator.comparing(BookCard::getPrice));
			
			double lowest = alternatives.get(0).getPrice(); // lowest price
			
			for(SearchResult result : resultSet) {
				for(BookCard b : result.getResult()) {
					if(b.equals(book) && b.getPrice() == lowest) b.setLowestPrice(true);
				}
			}
		}
	}
}
