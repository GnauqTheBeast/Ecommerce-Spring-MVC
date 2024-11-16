package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order checkout(List<Book> books) {
        Order order = new Order();
        double totalPrice = books.stream().mapToDouble(Book::getPrice).sum();
        order.setBooks(books);
        order.setTotalPrice(totalPrice);
        return order; // Save the order to the database if needed
    }
}
