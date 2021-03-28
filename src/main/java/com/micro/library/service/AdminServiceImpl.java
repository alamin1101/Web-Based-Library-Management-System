package com.micro.library.service;

import com.micro.library.entity.Book;
import com.micro.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AdminServiceImpl implements AdminService{


    @Autowired
    private BookRepository bookRepository;

    @Override
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public Book getBooktById(Integer bookId) {
        return bookRepository.findById(bookId).get();
    }

    @Override
    public void deleteBookById(Integer bookId)
    {
        bookRepository.deleteById(bookId);
    }

    @Override
    public List<Book> findByCatagory(String catagory)
    {
       return bookRepository.findByCategory(catagory);
    }

    @Override
    public List<Book> findAllBook() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findAllByBookTitleAndAndAuthor(String bookTitle, String author) {
        return bookRepository.findByBookTitleAndAndAuthor(bookTitle,author);
    }

    @Override
    public List<Book> findByBookTitle(String bookTitle) {
        return bookRepository.findByBookTitle(bookTitle);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }


    @Override
    public Map<Book, Integer> fetchbookbygroup() {
        Map<String, Integer> map = new HashMap<>();
        Map<String, Book> map2 = new HashMap<>();
        Map<Book, Integer> map3 = new HashMap<>();
        List<Book> books = bookRepository.findAll();
        books.stream().forEach(book -> {
            String catagory = book.getCategory();
            Integer count = map.get(catagory);
            if (count ==  null) {
                map.put(catagory, 1);
                map2.put(catagory, book);
            } else {
                map.put(catagory, map.get(catagory) + 1);
            }

        });

        Set<String> keys = map.keySet();
        for (String key : keys) {
            //System.out.println(map2.get(key) + " -> " + map.get(key));
            map3.put(map2.get(key), map.get(key));
        }

        return map3;
    }




}
