package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.entity.Customers;
import com.example.demo.service.BookService;
import com.example.demo.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/cart/add/{id}")
    public String addBookToCart(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        Customers customer = SessionUtils.getLoggedInCustomer(session);
        if (customer == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to add books to your cart.");
            return "redirect:/login";
        }

        bookService.addBookToCart(id);
        redirectAttributes.addFlashAttribute("message", "Book added to cart!");
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String viewBooks(Model model, HttpSession session) {
        Customers customer = SessionUtils.getLoggedInCustomer(session);

        List<Book> books = bookService.getAllBooks();
        model.addAttribute("customer", customer);
        model.addAttribute("books", books != null ? books : new ArrayList<>());
        return "product";
    }

    @GetMapping("/books/search")
    public String searchBooks(@RequestParam(value = "keyword", required = false) String keyword, Model model, HttpSession session) {
        Customers customer = SessionUtils.getLoggedInCustomer(session);

        List<Book> books = (keyword == null || keyword.trim().isEmpty())
                ? bookService.getAllBooks()
                : bookService.searchBooks(keyword.trim());

        model.addAttribute("customer", customer);
        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        model.addAttribute("isSearch", true);
        return "product";
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model, HttpSession session) {
        Customers customer = SessionUtils.getLoggedInCustomer(session);
        if (customer == null) {
            return "redirect:/login";
        }

        List<Book> books = bookService.getAllBooks();
        model.addAttribute("customer", customer);
        model.addAttribute("books", books);
        return "dashboard";
    }

    @GetMapping("/books/details/{id}")
    public String viewBookDetails(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findBookById(id);
        if (book == null) {
            return "redirect:/books";
        }
        model.addAttribute("book", book);
        return "product_details";
    }
}
