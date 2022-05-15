package com.tdonuk.scraper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.tdonuk.scraper.model.entity.Book;

class ScraperServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void regex() {
		String s = "123.234,49 tl";
		
		System.out.println(s);
		
		s = s.replaceAll("[.]", "").replaceAll(",", ".").replaceAll("[^0-9.]", "");
		
		System.out.println(Double.valueOf(s));
	}
	
	@Test
	public void comparator() {
		List<Integer> common = new ArrayList<Integer>();
		common.add(1);
		common.add(5);
		common.add(72);
		common.add(2);
		common.add(35);
		common.add(44);
		
		System.out.println("Before sorted: " + common);
		
		Collections.sort(common, Comparator.comparing(i -> i));
		
		System.out.println("after sorted: " + common);
	}
}
