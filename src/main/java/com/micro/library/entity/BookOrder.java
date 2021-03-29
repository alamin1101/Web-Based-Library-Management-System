package com.micro.library.entity;


import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@NoArgsConstructor

@Entity
public class BookOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_order_seq")
    @SequenceGenerator(name = "book_order_seq", sequenceName = "book_order_sequnce", allocationSize = 1)
    private int orderId;
    private String title;
    private String author;
    private String category;
    private String orderedDate;
    private String arivalDate;
    private long quantity;


    public BookOrder(String title, String author, String category, String orderedDate, String arivalDate, long quantity)
    {
        this.title = title;
        this.author = author;
        this.category = category;
        this.orderedDate = orderedDate;
        this.arivalDate = arivalDate;
        this.quantity = quantity;
    }
}
