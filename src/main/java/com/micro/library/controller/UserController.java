package com.micro.library.controller;

import com.micro.library.entity.AppUser;
import com.micro.library.entity.BookBorrow;
import com.micro.library.repository.AppUserRepository;
import com.micro.library.repository.BookRepository;
import com.micro.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import static com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS.required;

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
        model.addAttribute("users",appUserRepository.count());
        model.addAttribute("books",bookRepository.count());
        model.addAttribute("a_books",bookRepository.countBooksByAvailableIsTrue());
        return "user_home";
    }


    @RequestMapping("/user/book/category")
    public String userBookCatagory(Model model, @RequestParam(required = false) String s)
    {
        model.addAttribute("booklist",bookRepository.findAllBookNumbersByCategory(s));
        return "user_book_category";
    }


    @GetMapping("/user/book/title")
    public String userBooksTitle( Model model, @RequestParam( required = false) String s) {

        model.addAttribute("booklist",bookRepository.findAllBookNumbersByTitle(s));
        return "user_book_title";

    }

    @RequestMapping("/user/book/title/all")
    public String userBooksTitleall(@RequestParam String bookTitle,Model model)
    {
        model.addAttribute("booklist",bookRepository.findByBookTitle(bookTitle));
        return "user_bookself";
    }
    @RequestMapping("/user/book/category/all")
    public String adminBookCatagoryall(@RequestParam String category,Model model)
    {
        model.addAttribute("booklist",bookRepository.findByCategory(category));
        return "user_bookself";
    }


    @GetMapping("/user/borrow/booklist")
    public String adminBookUserBorrowlist(Principal principal, Model model) {
        AppUser appUser =  appUserRepository.findById(principal.getName()).get();
        if (appUser == null) {
            return "redirect:/user/book/category";
        }
        List<BookBorrow> bookBorrowList = (appUser.getBookBorrowList());
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
    public String userBookself(Model model,@RequestParam(required = false) String s)
    {
        model.addAttribute("booklist", bookRepository.findAllBooks(s));
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

    @GetMapping("/user/fine")
    public String fine(Model model,Principal principal)
    {
        AppUser appUser =  appUserRepository.findById(principal.getName()).get();
        List<BookBorrow> bookBorrowList = (appUser.getBookBorrowList());
        int totalFine=0;
        ListIterator<BookBorrow> litr = bookBorrowList.listIterator();
        while(litr.hasNext()){
            totalFine+=(litr.next().getFine());
        }
        model.addAttribute("fine",totalFine);
        return "user_fine";

    }



}
