package com.micro.library.repository;

import com.micro.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer>
{

    List<Book> findByCategory(String category);

    List<Book>findByBookTitleAndAndAuthor(String bookTitle,String author);

    List<Book>findByBookTitle(String bookTitle);

    List<Book>findByAuthor(String author);


    int countBooksByAvailableIsTrue();




    //@Query("select  count(b) from Book b")
    @Query("select new Book(b.bookTitle, b.category, count (b)) from Book b WHERE  (?1 is null or b.bookTitle like %?1%) GROUP BY b.bookTitle")
    List<Book> findAllBookNumbersByTitle(String title);


    @Query("select new Book(c.category, count (c)) from Book c WHERE  (?1 is null or c.category like %?1%) GROUP BY c.category")
    List<Book> findAllBookNumbersByCategory(String category);

    @Modifying
    @Query("update Book b set b.available=?1 where b.bookId=?2")
    void updateBookAvailability(boolean available, int bookId);

}
