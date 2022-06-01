package com.tdonuk.scraper.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.tdonuk.scraper.model.BookCard;
import com.tdonuk.scraper.model.SearchResultBookEntity;
import com.tdonuk.scraper.util.BookSourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tdonuk.scraper.comm.BookServiceClient;
import com.tdonuk.scraper.model.ResultWrapper;
import com.tdonuk.scraper.model.SearchResult;
import com.tdonuk.scraper.model.entity.Book;
import com.tdonuk.scraper.service.ScraperService;

@RestController
@RequestMapping("/api/search")
public class ScraperController {
	@Autowired
	private ScraperService scraperService;
	
	@Autowired
	private BookServiceClient bookService;
	
	
	@GetMapping("")
	public ResponseEntity<?> search(@RequestParam String key, HttpServletRequest request) {
		try {
			ResultWrapper resultSet = scraperService.searchDefault(key);

			String authorization = request.getHeader("Authorization");

			List<BookCard> common = resultSet.getCommon();
			List<BookCard> persisted = bookService.saveBooks(common, authorization);

			Set<String> sources = persisted.stream().map(b -> b.getSource()).collect(Collectors.toSet());

			List<SearchResult> persistedResults = new ArrayList<>();
			for(String source: sources) {
				SearchResult result = new SearchResult(BookSourceUtils.getFromText(source), persisted.stream().filter(b -> b.getSource().equals(source)).collect(Collectors.toSet()));

				persistedResults.add(result);
			}

			ResultWrapper persistedResultset = new ResultWrapper(persistedResults);
			
			return ResponseEntity.ok(persistedResultset);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Servis geçici olarak kullanılamamaktadır. Lütfen daha sonra tekrar deneyiniz.");
		}
	}
	
}
