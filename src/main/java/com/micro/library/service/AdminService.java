package com.micro.library.service;

import com.micro.library.entity.Book;

import java.util.List;
import java.util.Map;

public interface AdminService {
    void addBook(Book book);
    Book getBooktById(Integer bookId);
    void deleteBookById(Integer bookId);
    List<Book> findByCatagory(String catagory);
    Map<Book, Integer> fetchbookbygroup();
    List<Book> findAllBook();
    List<Book>findAllByBookTitleAndAndAuthor(String bookTitle,String author);
    List<Book>findByBookTitle(String bookTitle);
    List<Book>findByAuthor(String author);

}
