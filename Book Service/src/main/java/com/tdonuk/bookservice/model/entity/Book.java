package com.tdonuk.bookservice.model.entity;

import com.tdonuk.bookservice.model.Author;
import lombok.Data;
import org.hibernate.annotations.Fetch;

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
    private String isbn;
    private String language;
    private String description;
    private String imageUrl;
    private String url;

    private float rating;
    private long rateCount;

    private Date publishDate;
    private Date creationDate;
}
