package com.tdonuk.scraper.adapter;

import lombok.Data;

@Data
public class CssQueryPaths {
	// listing page
	private String bookUrl;
	private String pageBar;
	
	// details page
	private String imageUrl;
	private String title;
	private String description;
	private String price;
	private String isbn;
	private String rating;
	private String rateCount;
	private String tags;
	private String language;
	private String pageCount;
	private String author;
	private String publishDate;

	
	private CssQueryPaths(CssQueryPathBuilder builder) {
		setBookUrl(builder.bookUrl);
		setPageBar(builder.pageBar);
		setImageUrl(builder.imageUrl);
		setTitle(builder.title);
		setDescription(builder.description);
		setPrice(builder.price);
		setIsbn(builder.isbn);
		setRating(builder.rating);
		setRateCount(builder.rateCount);
		setTags(builder.tags);
	}


	public static class CssQueryPathBuilder {
		private String bookUrl;
		private String pageBar;
		private String imageUrl;
		private String title;
		private String description;
		private String price;
		private String isbn;
		private String rating;
		private String rateCount;
		private String tags;
		private String language;
		private String pageCount;
		private String author;
		private String publishDate;
		
		private static CssQueryPathBuilder builder = new CssQueryPathBuilder();
		
		public static CssQueryPathBuilder bookUrl(final String url) {
			builder.bookUrl = url;
			
			return builder;
		}
		
		public static CssQueryPathBuilder pageBar(final String pageBar) {
			builder.pageBar = pageBar;
			
			return builder;
		}
		
		public static CssQueryPathBuilder imageUrl(final String imageUrl) {
			builder.imageUrl = imageUrl;
			
			return builder;
		}
		
		public static CssQueryPathBuilder title(final String title) {
			builder.title = title;
			
			return builder;
		}
		
		public static CssQueryPathBuilder description(final String description) {
			builder.description = description;
			
			return builder;
		}
		
		public static CssQueryPathBuilder price(final String price) {
			builder.price = price;
			
			return builder;
		}
		
		public static CssQueryPathBuilder isbn(final String isbn) {
			builder.isbn = isbn;
			
			return builder;
		}
		
		public static CssQueryPathBuilder rating(final String rating) {
			builder.rating = rating;
			
			return builder;
		}
		
		public static CssQueryPathBuilder rateCount(final String rateCount) {
			builder.rateCount = rateCount;
			
			return builder;
		}
		
		public static CssQueryPathBuilder tags(final String tags) {
			builder.tags = tags;
			
			return builder;
		}
		
		public static CssQueryPathBuilder language(final String language) {
			builder.language = language;
			
			return builder;
		}
		
		public static CssQueryPathBuilder pageCount(final String pageCount) {
			builder.pageCount = pageCount;
			
			return builder;
		}
		
		public static CssQueryPathBuilder author(final String author) {
			builder.author = author;
			
			return builder;
		}
		
		public static CssQueryPathBuilder publishDate(final String publishDate) {
			builder.publishDate = publishDate;
			
			return builder;
		}
		
		public static CssQueryPaths build() {
			return new CssQueryPaths(builder);
		}
	}
}
