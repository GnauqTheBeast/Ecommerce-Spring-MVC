package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    private List<Book> cartBooks = new ArrayList<>();

    // Lấy tất cả các sách từ cơ sở dữ liệu
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    public Book findBookById(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        return book.orElse(null);
    }

    public List<Book> getCartBooks() {
        return cartBooks;
    }

    public void addBookToCart(Long bookId) {
        Book book = findBookById(bookId);
        if (book != null) {
            cartBooks.add(book);
        }
    }

    public void clearCart() {
        cartBooks.clear();
    }

    public int getCartSize() {
        return cartBooks.size();
    }
}