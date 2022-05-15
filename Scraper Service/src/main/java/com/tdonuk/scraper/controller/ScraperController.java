package com.tdonuk.scraper.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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
	
	private LocalDateTime lastRequestDate;
	
	private ResultWrapper lastResult = null;
	
	
	@GetMapping("")
	public ResponseEntity<?> search(@RequestParam String key, HttpServletRequest request) {
		try {
			ResultWrapper result = scraperService.searchDefault(key);
			
			String authorization = request.getHeader("Authorization");
			
			for(SearchResult resultSet : result.getResultSet()) {
				resultSet.setResult(bookService.saveBooks(resultSet.getResult(), authorization));
			}
			
			lastResult = result;
			
			return ResponseEntity.ok(result);
		} catch(Exception e) {
			return ResponseEntity.badRequest().body("Servis geçici olarak kullanılamamaktadır. Lütfen daha sonra tekrar deneyiniz.");
		}

	}
}
