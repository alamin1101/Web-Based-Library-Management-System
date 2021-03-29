package com.micro.library.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @SequenceGenerator(name = "book_sequence", sequenceName = "book_sequence", allocationSize = 1,initialValue = 101)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "")
    private int bookId;
    private String bookTitle;
    private String author;
    private String category;
    private String edition;
    private String price;
    private boolean available=true;
    private Long availablebooks = 1l;

    public Book(String bookTitle, String author, String category, String edition, String price, boolean available) {
        this.bookTitle = bookTitle;
        this.author = author;
        this.category = category;
        this.edition = edition;
        this.price = price;
        this.available= available;
    }

    public Book(String bookTitle, String category, Long availablebooks) {
        this.bookTitle = bookTitle;
        this.category = category;
        this.availablebooks = availablebooks;
    }
    public Book(String category, Long availablebooks)
    {
        this.category = category;
        this.availablebooks = availablebooks;
    }

}

