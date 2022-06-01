package com.tdonuk.bookservice.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tdonuk.bookservice.model.Name;
import com.tdonuk.bookservice.model.Phone;
import com.tdonuk.bookservice.model.UserRole;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @Embedded
    private Name name;

    @Embedded
    private Phone primaryPhone;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(unique = true)
    private String email;

    private String username;
    private String password;

    private Date birthDate;

    private Date accountCreationDate;
    private Date lastLoginDate;
    private Date lastLogoutDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "book_favourites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private Set<SearchResultBookEntity> searches;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "book_searches",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private Set<SearchResultBookEntity> favourites;
}
