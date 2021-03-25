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
    private String catagory;
    private String edition;
    private String price;
    private boolean available=true;
    private Long availablebooks= Long.valueOf(1);

    public Book( String bookTitle, String author, String catagory, String edition, String price, boolean available) {
        this.bookTitle = bookTitle;
        this.author = author;
        this.catagory = catagory;
        this.edition = edition;
        this.price = price;
        this.available= available;
    }

    public Book(String bookTitle,String catagory, Long availablebooks) {
        this.bookTitle = bookTitle;
        this.catagory = catagory;
        this.availablebooks = availablebooks;
    }
    public Book(String catagory, Long availablebooks) {
        this.catagory = catagory;
        this.availablebooks = availablebooks;
    }

}

