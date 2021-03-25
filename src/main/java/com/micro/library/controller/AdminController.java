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
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

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
    public String admin()
    {
        return "admin_home";
    }



    @GetMapping("/admin/book/add")
    public String adminBookAdd(Model model)
    {
        model.addAttribute("book", new Book());
        return "admin_book_add";
    }

    @PostMapping("/admin/book/add")
    public String adminBookAdding(@Valid Book book)
    {

        adminService.addBook(book);
        return "redirect:/admin/book/catagory";
    }


    @RequestMapping("/admin/book/catagory")
    public String adminBookCatagory(Model model)
    {
        model.addAttribute("booklist",bookRepository.findAllBookNumbersByCatagory());
        return "admin_book_catagory";
    }


    @GetMapping("/admin/book/title")
    public String adminBooksTitle( Model model) {

        model.addAttribute("booklist",bookRepository.findAllBookNumbersByTitle());
        return "admin_book_title";

    }

    @RequestMapping("/admin/book/title/all")
    public String adminBooksTitle(@RequestParam String bookTitle,Model model)
    {
        model.addAttribute("booklist",bookRepository.findByBookTitle(bookTitle));
        return "admin_bookself";
    }

    @RequestMapping("/admin/book/catagory/all")
    public String adminBookCatagory(@RequestParam String catagory,Model model)
    {
        model.addAttribute("booklist",bookRepository.findByCatagory(catagory));
        return "admin_bookself";
    }



    @GetMapping("/admin/book/update")
    public String adminBookUpdate(@RequestParam int bookId, Model model){
        model.addAttribute("book", adminService.getBooktById(bookId));
        return "admin_book_update";
    }
    @PostMapping("/admin/book/update")
    public String adminUpdatesuccess(@Valid Book book)
    {

        adminService.addBook(book);
        return "redirect:/admin/book/catagory";
    }

    @RequestMapping("/admin/book/delete")
    public String adminBookDelete(@RequestParam int bookId){
        adminService.deleteBookById(bookId);
        return "redirect:/admin/book/catagory";
    }

    @RequestMapping("/admin/bookself")
    public String adminBookself(Model model){
        model.addAttribute("booklist", adminService.findAllBook());
        return "admin_bookself";
    }

    @RequestMapping("admin/userslist")
    public String adminUserslist(Model model) {
        AppUser appUser=new AppUser();
        model.addAttribute("userslist", appUserRepository.findAllUsers());
        return "admin_list_of_users";
    }



    @RequestMapping("/admin/search")
    public String userSearch(@RequestParam String username, Model model)
    {

        model.addAttribute("userslist", appUserRepository.findById(username).get());
        return "admin_list_of_users";
    }






    @GetMapping("/admin/book/order")
    public String adminBookOrder1()
    {
        return "admin_book_order";
    }


    @PostMapping("/admin/book/order")
    public String adminBookOrder(@ModelAttribute BookOrder bookorder) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (format.parse(bookorder.getArivalDate()).before(format.parse(bookorder.getOrderedDate()))) {
            return "admin_book_order";
        }
        bookOrderRepository.save(bookorder);
        return "redirect:/admin/book/orderlist";
    }

    @GetMapping("/admin/book/orderlist")
    public String adminBookOrderlist(Model model)
    {
        model.addAttribute("orderlist",bookOrderRepository.findAll());
        return "admin_book_orderlist";
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
        return "admin_user_borrow_book";
    }

    @PostMapping("/admin/user/borrow/book")
    public String userBorrowBook(@RequestParam int bookId,@RequestParam String username,@RequestParam String borrowDate,@RequestParam String submitDate) {

        Book book = bookRepository.findById(bookId).get();
        AppUser appUser = appUserRepository.findById(username).get();
        if (book == null||appUser.getRole().equals("ROLE_ADMIN")||appUser==null)
        {
            return "redirect:/admin/user/borrow/book";
        }

        book.setAvailable(false);



        BookBorrow bookBorrow = new BookBorrow();
        bookBorrow.setBook(book);
        bookBorrow.setSubmitDate(submitDate);
        bookBorrow.setBorrowDate(borrowDate);
        bookBorrow.setReturned(false);
        appUser.getBookBorrowList().add(bookBorrow);

        appUserRepository.save(appUser);

        return "redirect:/admin/book/catagory";
    }




    @GetMapping("/admin/book/borrowerslist")
    public String fetchBorrowBookInfo(Model model) {
        List<AppUser> appUsers = appUserRepository.findAllByRole("ROLE_USER");
        appUsers.forEach(u->{
            u.setBookBorrowList(u.getBookBorrowList()
                    .stream().filter(b->!b.isReturned())
                    .collect(Collectors.toList()));
        });
        model.addAttribute("borrowlist",appUsers);
        return "admin_book_borrowers";
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
        return "admin_book_borrows_all";
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



}
