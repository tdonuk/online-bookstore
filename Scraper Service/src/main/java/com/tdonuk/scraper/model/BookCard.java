package com.tdonuk.scraper.model;

import com.tdonuk.scraper.model.BookSource;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.text.WordUtils;
import org.apache.commons.text.diff.StringsComparator;
import org.apache.commons.text.similarity.CosineDistance;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class BookCard {
	private long id;
	private String title;
	private double price;
	private String url;
	private String imgUrl;
	private String authors; // comma-seperated names
	private String source;
	private String publisher;
	boolean isLowestPrice = false;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BookCard bookCard = (BookCard) o;
		return title.equals(bookCard.title) && authors.equals(bookCard.authors) && publisher.equals(bookCard.publisher);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, authors, publisher);
	}
}
