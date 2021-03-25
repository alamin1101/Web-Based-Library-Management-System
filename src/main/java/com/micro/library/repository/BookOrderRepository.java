package com.micro.library.repository;

import com.micro.library.entity.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookOrderRepository  extends JpaRepository<BookOrder, Integer>
{

}
