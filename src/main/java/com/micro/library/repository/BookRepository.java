package com.micro.library.repository;

import com.micro.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer>
{

    List<Book> findByCatagory(String catagory);
    List<Book>findByBookTitleAndAndAuthor(String bookTitle,String author);
    List<Book>findByBookTitle(String bookTitle);
    List<Book>findByAuthor(String author);

    //@Query("select  count(b) from Book b")
    @Query("select new Book(b.bookTitle, b.catagory, count (b)) from Book b WHERE b.available=true GROUP BY b.bookTitle")
    List<Book> findAllBookNumbersByTitle();


    @Query("select new Book(c.catagory, count (c)) from Book c WHERE c.available=true GROUP BY c.catagory")
    List<Book> findAllBookNumbersByCatagory();

    @Modifying
    @Query("update Book b set b.available=?1 where b.bookId=?2")
    void updateBookAvailability(boolean available, int bookId);

}
