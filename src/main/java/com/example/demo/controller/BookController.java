package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/cart/add/{id}")
    public String addBookToCart(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        bookService.addBookToCart(id);
        redirectAttributes.addFlashAttribute("message", "Book added to cart!");
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String viewBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books != null ? books : new ArrayList<>());
        return "books";
    }

    @GetMapping("/books/search")
    public String searchBooks(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Book> books;
        if (keyword == null || keyword.trim().isEmpty()) {
            books = bookService.getAllBooks();
        } else {
            books = bookService.searchBooks(keyword.trim());
        }

        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        model.addAttribute("isSearch", true);
        return "books";
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model, Principal principal) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);

        if (principal != null) {
            model.addAttribute("customer", principal.getName());
        }
        return "dashboard";
    }

    @GetMapping("/books/details/{id}")
    public String viewBookDetails(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findBookById(id);
        if (book == null) {
            return "redirect:/books";
        }
        model.addAttribute("book", book);
        return "book_details";
    }
}
