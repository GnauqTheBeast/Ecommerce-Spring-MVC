package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class WelcomeController {

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String viewDashboard(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();

            List<Book> books = bookService.getAllBooks();

            model.addAttribute("books", books);
            model.addAttribute("username", username);
            return "dashboard";
        }
        return "welcome";
    }
}
