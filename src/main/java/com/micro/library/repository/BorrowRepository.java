package com.micro.library.repository;

import com.micro.library.entity.Book;
import com.micro.library.entity.BookBorrow;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface BorrowRepository extends CrudRepository<BookBorrow, Long> {


    @Modifying
    @Query("delete from BookBorrow bb where bb.book=?1")
    void removeByBorrowBook(Book book);


}
