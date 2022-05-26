package com.tdonuk.scraper.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.text.WordUtils;
import org.apache.commons.text.diff.StringsComparator;
import org.apache.commons.text.similarity.CosineDistance;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class BookCard {
	private String title;
	private double price;
	private String url;
	private String imgUrl;
	private String authors; // comma-seperated names
	private BookSource source;
	private String publisher;
	boolean isLowestPrice = false;
}
