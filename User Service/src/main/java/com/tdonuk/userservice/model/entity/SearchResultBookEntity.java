package com.tdonuk.userservice.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "search_results")
public class SearchResultBookEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String title;
	private double price;
	private String url;
	private String imgUrl;
	private String authors;
	private String source;
	private String publisher;
	boolean isLowestPrice = false;
	private Date lastSeenInSearch;
	private long favouriteCount = 0;

	@ManyToMany(mappedBy = "favourites", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JsonIgnore
	private Set<UserEntity> favouriters;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SearchResultBookEntity that = (SearchResultBookEntity) o;
		return id == that.id && title.equals(that.title) && authors.equals(that.authors) && source.equals(that.source) && publisher.equals(that.publisher);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, authors, source, publisher);
	}
}
