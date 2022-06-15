package com.tdonuk.bookservice.service;

import com.tdonuk.bookservice.comm.UserClient;
import com.tdonuk.bookservice.model.Author;
import com.tdonuk.bookservice.model.Name;
import com.tdonuk.bookservice.model.UserDTO;
import com.tdonuk.bookservice.model.entity.Book;
import com.tdonuk.bookservice.model.entity.SearchResultBookEntity;
import com.tdonuk.bookservice.model.entity.UserEntity;
import com.tdonuk.bookservice.model.repository.BookEntityPageableRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class BookServiceTest {
    @Autowired
    private BookService service;

    @Autowired
    private UserClient client;
    @Autowired
    private BookEntityPageableRepository bookEntityPageableRepository;

    @Test
    void canLoadBook() {
        Book book = new Book();

        Author a = new Author(new Name("Taha", "Dönük"));
        Author b = new Author(new Name("Resul", "Taha"));
        book.setAuthors(Set.of(a,b));

        Date date = new Date(1999, 12, 2);
        book.setPublishDate(date);

        book.setDescription("Örnek kitap açıklaması");
        book.setTitle("Örnek Kitap İsmi");
        book.setInDiscount(true);
        book.setIsbn("123456");
        book.setLanguage("tr");
        book.setPrice(39);
        book.setRateCount(120);
        book.setRating(4.7f);
        book.setTags(Set.of("Macera", "Bilim Kurgu", "Polisiye"));
        book.setPageCount(419);

        service.save(book);
    }

    @Test
    void canFindBookWithAuthor() {
        long id = 8;

        Book b = service.findById(id);

        System.out.println(b.getAuthors());

        Author a = new Author(new Name("Taha", "Dönük"));

        System.out.println(service.findAllByAuthor(a));
    }

    @Test
    void canGetUser() {
        // example token
        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YWhhMiIsInJvbGVzIjpbIlVTRVIiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDAwL2FwaS91c2VyL2xvZ2luIiwiZXhwIjoxNjUxNzgxMzIzfQ.Nnr0hV_qDbD0wpbAQXl35W08fS0dU9EgODtEZx0Wg5w";
        UserEntity user = client.getUser(token);

        System.out.println(user);
    }

    @Test
    void canGetMostLiked() {
        Pageable p = PageRequest.of(0 ,10);
        Page<SearchResultBookEntity> books = bookEntityPageableRepository.findAll(p);

        books.getContent().forEach(e -> System.out.println(e.getFavouriters().size()));
    }
}
