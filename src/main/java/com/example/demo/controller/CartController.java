package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.entity.Order;
import com.example.demo.service.BookService;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BookService bookService;

    // Display the cart page
//    @GetMapping("/cart")
//    public String viewCart(Model model) {
//        List<Book> cartBooks = bookService.getCartBooks();
//        model.addAttribute("cartBooks", cartBooks);
//        return "cart";
//    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        List<Book> cartBooks = bookService.getCartBooks();
        model.addAttribute("cartBooks", cartBooks);
        model.addAttribute("cartSize", bookService.getCartSize());
        return "cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        List<Book> cartBooks = bookService.getCartBooks();
        if (cartBooks.isEmpty()) {
            model.addAttribute("message", "Your cart is empty!");
            return "cart";
        }

        Order order = orderService.checkout(cartBooks);
        model.addAttribute("order", order);
        model.addAttribute("message", "Order placed successfully!");

        bookService.clearCart();

        return "order_confirmation";
    }


    // Add book to cart
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long bookId) {
        bookService.addBookToCart(bookId);
        return "redirect:/books";
    }
}