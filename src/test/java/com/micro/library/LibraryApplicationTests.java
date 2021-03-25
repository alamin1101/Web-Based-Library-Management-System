package com.micro.library;

import com.micro.library.entity.AppUser;
import com.micro.library.entity.Book;
import com.micro.library.entity.BookBorrow;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
class LibraryApplicationTests {

    @Test
    void contextLoads() throws ParseException {

    }

    public static void main(String[] args) throws ParseException {
        AppUser appUser = new AppUser();
        appUser.setUsername("Limon");
        BookBorrow b1 = new BookBorrow();
        b1.setBook(new Book());
        b1.setReturned(true);

        BookBorrow b2 = new BookBorrow();
        b2.setBook(new Book());
        b2.setReturned(false);

        appUser.setBookBorrowList(Arrays.asList(b1, b2));
        List<AppUser> appUsers = Arrays.asList(
                appUser
        );
        appUsers.forEach(u->{
            u.setBookBorrowList(u.getBookBorrowList().stream().filter(b->!b.isReturned()).collect(Collectors.toList()));
        });
        System.out.println(appUsers.get(0).getBookBorrowList().get(0).isReturned());
    }

}
