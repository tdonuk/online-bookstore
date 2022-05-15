package com.tdonuk.scraper.model.entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;

import com.tdonuk.scraper.model.Author;
import com.tdonuk.scraper.model.BookSource;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> tags;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Author> authors;

    private double price;

    private boolean inDiscount;

    private int pageCount;

    private String title;
    
    @Column(unique = true)
    private String isbn;
    private String language;
    private String description;
    private String imageUrl;
    private String url;

    private float rating;
    private long rateCount;

    private Date publishDate;
    private Date creationDate;
    
    @Enumerated(EnumType.STRING)
    private BookSource source;
    
    private boolean isLowestPrice;
    
    private Date lastSeenInSearch;
}
