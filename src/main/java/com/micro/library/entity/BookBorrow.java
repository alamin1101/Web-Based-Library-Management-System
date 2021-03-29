package com.micro.library.entity;

import lombok.*;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class BookBorrow {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_borrow_seq")
    @SequenceGenerator(name = "book_borrow_seq", sequenceName = "book_borrow_sequnce", allocationSize = 1)
    private long borrowId;
    @OneToOne
    @JoinColumn(name = "bookinfo", referencedColumnName = "bookId")
    private Book book;
    private String borrowDate;
    private String submitDate;
    private boolean isReturned;
    @Transient
    private int fine;

    public void setBorrowId(long borrowId) {
        this.borrowId = borrowId;
    }

    public int getFine() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date submitDate = format.parse(this.submitDate);
            Date returnDate = new Date();
            int dayCount = 0;
            if (returnDate.toInstant().isAfter(submitDate.toInstant())) {
                dayCount = (int) ((returnDate.getTime() - submitDate.getTime()) / (1000 * 60 * 60 * 24));
            }
            this.fine = 5 * dayCount;
        }catch (Exception e) {
            System.err.println("Ex: " + e.getMessage());
        }

        return fine;
    }
}
