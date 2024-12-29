package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.entity.Customers;
import com.example.demo.service.BookService;
import jakarta.servlet.http.HttpSession;
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
    public String viewDashboard(Model model, HttpSession session) {
        Customers customer = (Customers) session.getAttribute("customers");
        if (customer != null) {
            String username = customer.getUsername();
            List<Book> books = bookService.getAllBooks();

            model.addAttribute("books", books);
            model.addAttribute("username", username);
            return "dashboard";
        }
        return "welcome";
    }
}
