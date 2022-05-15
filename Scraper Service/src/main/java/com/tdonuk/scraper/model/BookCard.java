package com.tdonuk.scraper.model;

import lombok.Data;

@Data
public class BookCard {
	private String title;
	private String price;
	private String url;
	private String imgUrl;
}
