package com.micro.library.controller;

import com.micro.library.entity.AppUser;
import com.micro.library.entity.Book;
import com.micro.library.entity.BookBorrow;
import com.micro.library.repository.AppUserRepository;
import com.micro.library.repository.BookRepository;
import com.micro.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminService adminService;
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    private BookRepository bookRepository;

    @RequestMapping("/user")
    public String admin(Model model)
    {
        return "user_home";
    }


    @RequestMapping("/user/book/catagory")
    public String userBookCatagory(Model model)
    {
        model.addAttribute("booklist",bookRepository.findAllBookNumbersByCatagory());
        return "user_book_catagory";
    }


    @GetMapping("/user/book/title")
    public String userBooksTitle( Model model) {

        model.addAttribute("booklist",bookRepository.findAllBookNumbersByTitle());
        return "user_book_title";

    }

    @RequestMapping("/user/book/title/all")
    public String userBooksTitleall(@RequestParam String bookTitle,Model model)
    {
        model.addAttribute("booklist",bookRepository.findByBookTitle(bookTitle));
        return "user_bookself";
    }
    @RequestMapping("/user/book/catagory/all")
    public String adminBookCatagoryall(@RequestParam String catagory,Model model)
    {
        model.addAttribute("booklist",bookRepository.findByCatagory(catagory));
        return "user_bookself";
    }


    @GetMapping("/user/borrow/booklist")
    public String adminBookUserBorrowlist(Principal principal, Model model) {
        AppUser appUser =  appUserRepository.findById(principal.getName()).get();
        if (appUser == null) {
            return "redirect:/user/book/catagory";
        }
        List<BookBorrow> bookBorrowList = appUser.getBookBorrowList();
        model.addAttribute("bookBorrowList", bookBorrowList);
        model.addAttribute("username", principal.getName());
        return "user_borrow_booklist";
    }





    @ResponseBody
    @RequestMapping("/fetch_user/{username}")
    public AppUser fetchUser(@PathVariable("username") String username) {
        return appUserRepository.findById(username).get();
    }



    @RequestMapping("/user/bookself")
    public String userBookself(Model model) {
        model.addAttribute("booklist", adminService.findAllBook());
        return "user_bookself";
    }




    @GetMapping("/user/book/borrow/list")
    public String  borrowHistory(Principal principal,Model model ) {
        AppUser appUser = appUserRepository.findById(principal.getName()).get();
        BookBorrow bookBorrow=new BookBorrow();
        Date date=new Date();
        model.addAttribute("borrowlist",appUser.getBookBorrowList());
        return "user_borrow_booklist";
    }



}
