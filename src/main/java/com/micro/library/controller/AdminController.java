package com.micro.library.controller;


import com.micro.library.entity.AppUser;
import com.micro.library.entity.Book;
import com.micro.library.entity.BookBorrow;
import com.micro.library.entity.BookOrder;
import com.micro.library.repository.AppUserRepository;
import com.micro.library.repository.BookOrderRepository;
import com.micro.library.repository.BookRepository;
import com.micro.library.repository.BorrowRepository;
import com.micro.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private BookOrderRepository bookOrderRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    @RequestMapping("/admin")
    public String admin(Model model)
    {
        model.addAttribute("users",appUserRepository.count());
        model.addAttribute("books",bookRepository.count());
        model.addAttribute("a_books",bookRepository.countBooksByAvailableIsTrue());
        return "admin_home";
    }



    @GetMapping("/admin/book/add")
    public String adminBookAdd(Model model)
    {
        model.addAttribute("book", new Book());
        return "book_add";
    }

    @PostMapping("/admin/book/add")
    public String adminBookAdding(@Valid Book book)
    {

        adminService.addBook(book);
        return "redirect:/admin/book/category";
    }


    @RequestMapping("/admin/book/category")
    public String adminBookCatagory(Model model, @RequestParam(required = false) String s)
    {
        model.addAttribute("booklist",bookRepository.findAllBookNumbersByCategory(s));
        return "book_category";
    }


    @GetMapping("/admin/book/title")
    public String adminBooksTitle(Model model, @RequestParam(required = false) String s) {

        model.addAttribute("booklist",bookRepository.findAllBookNumbersByTitle(s));
        return "book_title";

    }

    @RequestMapping("/admin/book/title/all")
    public String adminBooksTitle(@RequestParam String bookTitle,Model model)
    {
        model.addAttribute("booklist",bookRepository.findByBookTitle(bookTitle));
        return "bookself";
    }

    @RequestMapping("/admin/book/category/all")
    public String adminBookCatagory(@RequestParam String category,Model model)
    {
        model.addAttribute("booklist",bookRepository.findByCategory(category));
        return "bookself";
    }



    @GetMapping("/admin/book/update")
    public String adminBookUpdate(@RequestParam int bookId, Model model){
        model.addAttribute("book", adminService.getBooktById(bookId));
        return "book_update";
    }
    @PostMapping("/admin/book/update")
    public String adminUpdatesuccess(@Valid Book book)
    {

        adminService.addBook(book);
        return "redirect:/admin/book/category";
    }

    @RequestMapping("/admin/book/delete")
    public String adminBookDelete(@RequestParam int bookId){
        adminService.deleteBookById(bookId);
        return "redirect:/admin/book/category";
    }

    @RequestMapping("/admin/bookself")
    public String adminBookself(Model model){
        model.addAttribute("booklist", adminService.findAllBook());
        return "bookself";
    }

    @RequestMapping("admin/userslist")
    public String adminUserslist(Model model,@RequestParam(required = false) String s) {
        AppUser appUser=new AppUser();
        model.addAttribute("userslist", appUserRepository.findAllUsers(s));
        return "list_of_users";
    }



    @RequestMapping("/admin/search")
    public String userSearch(@RequestParam String username, Model model)
    {

        model.addAttribute("userslist", appUserRepository.findById(username).get());
        return "list_of_users";
    }






    @GetMapping("/admin/book/order")
    public String adminBookOrder1()
    {
        return "book_order";
    }


    @PostMapping("/admin/book/order")
    public String adminBookOrder(@ModelAttribute BookOrder bookorder) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (format.parse(bookorder.getArivalDate()).before(format.parse(bookorder.getOrderedDate()))) {
            return "book_order";
        }
        bookOrderRepository.save(bookorder);
        return "redirect:/admin/book/orderlist";
    }

    @GetMapping("/admin/book/orderlist")
    public String adminBookOrderlist(Model model)
    {
        model.addAttribute("orderlist",bookOrderRepository.findAll());
        return "book_orderlist";
    }



    @RequestMapping("/admin/book/order/delete")
    public String adminBookOrderDelete(@RequestParam int orderId)
    {
        bookOrderRepository.deleteById(orderId);
        return "redirect:/admin/book/orderlist";

    }

    @GetMapping("/admin/user/borrow/book")
    public String userBookBorrow(@RequestParam int bookId, Model model){
        model.addAttribute("book", adminService.getBooktById(bookId));
        return "user_borrow_book";
    }

    @PostMapping("/admin/user/borrow/book")
    public String userBorrowBook(Model model,@RequestParam int bookId,@RequestParam String username,@RequestParam String borrowDate,@RequestParam String submitDate) {

        Book book = bookRepository.findById(bookId).get();
        AppUser appUser = appUserRepository.findById(username).orElse(null);
        if (appUser.getRole().equals("ROLE_ADMIN"))
        {
            return "redirect:/home";

        }

        book.setAvailable(false);

        BookBorrow bookBorrow = new BookBorrow();
        bookBorrow.setBook(book);
        bookBorrow.setSubmitDate(submitDate);
        bookBorrow.setBorrowDate(borrowDate);
        bookBorrow.setReturned(false);
        appUser.getBookBorrowList().add(bookBorrow);

        appUserRepository.save(appUser);

        return "redirect:/admin/book/category";
    }




    @GetMapping("/admin/book/borrowerslist")
    public String fetchBorrowBookInfo(Model model, @RequestParam(required = false) String s) {
        List<AppUser> appUsers = appUserRepository.findAllByRoleAndSearchValue(s, "ROLE_USER");
        model.addAttribute("borrowlist",appUsers);
        return "book_borrowers";
    }

    @GetMapping("/admin/borrow/books")
    public String adminBookUserBorrowlist(@RequestParam(name = "user") String username, Model model) {
        AppUser appUser =  appUserRepository.findById(username).get();
        if (appUser == null) {
            return "redirect:/admin/userslist";
        }
        List<BookBorrow> bookBorrowList = appUser.getBookBorrowList();
        model.addAttribute("bookBorrowList", bookBorrowList);
        model.addAttribute("username", username);
        return "borrow_all_books";
    }

    @Transactional
    @GetMapping("/admin/book/return")
    public String adminBookReturn(@RequestParam("book") int bookId, @RequestParam(name = "user") String username, Model model) {

        bookRepository.updateBookAvailability(true, bookId);
        Book book = bookRepository.findById(bookId).get();
        borrowRepository.removeByBorrowBook(book);

        return "redirect:/admin/book/borrowerslist";
    }


    @GetMapping("/admin/book/borrowers")
    public String m()
    {
        return "redirect:/admin/book/borrowerslist";
    }
//    @GetMapping("/admin/search")
//    public String search(@RequestParam String username,Model model)
//    {
//        model.addAttribute("list",appUserRepository.findById(username).get());
//        return "/search_page";
//    }



}
