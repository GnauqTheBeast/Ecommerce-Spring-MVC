package com.example.demo.controller;

import com.example.demo.entity.Admin;
import com.example.demo.entity.AdminRole;
import com.example.demo.entity.Book;
import com.example.demo.entity.Customers;
import com.example.demo.service.AdminService;
import com.example.demo.service.BookService;
import com.example.demo.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CustomerService customerService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("login")
    public String showAdminLoginForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin_login";
    }


    @PostMapping("/login")
    public String adminLogin(@ModelAttribute("admin") Admin admin, Model model, HttpSession session) {
        boolean isAuthenticated = adminService.authenticateAdmin(admin.getUsername(), admin.getPassword());
        if (isAuthenticated) {
            Admin authenticatedAdmin = adminService.findByAdminUsername(admin.getUsername());
            session.setAttribute("admin", authenticatedAdmin);
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid admin username or password");
        return "admin_login";
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("role", admin.getRole());
        return "admin_dashboard";
    }

    @GetMapping("/add-product")
    public String showAddProductForm(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null || !(admin.getRole() == AdminRole.SUPER_ADMIN || admin.getRole() == AdminRole.PRODUCT_ADMIN)) {
            model.addAttribute("error", "You don't have permission to add products.");
            return "admin_dashboard";
        }
        model.addAttribute("book", new Book());
        return "add_book";
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute("book") Book book, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null || !(admin.getRole() == AdminRole.SUPER_ADMIN || admin.getRole() == AdminRole.PRODUCT_ADMIN)) {
            model.addAttribute("error", "You don't have permission to add products.");
            return "admin_dashboard";
        }
        bookService.addBook(book);
        model.addAttribute("success", "Product added successfully!");
        return "admin_dashboard";
    }

    @GetMapping("/manage-products")
    public String showManageProducts(@RequestParam(value = "keyword", required = false) String keyword, Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        if (admin.getRole() != AdminRole.SUPER_ADMIN && admin.getRole() != AdminRole.PRODUCT_ADMIN) {
            model.addAttribute("error", "You do not have permission to manage products.");
            return "admin_dashboard";
        }

        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("books", bookService.searchBooks(keyword));
        } else {
            model.addAttribute("books", bookService.getAllBooks());
        }
        model.addAttribute("keyword", keyword);
        return "manage_products";
    }

    @GetMapping("/edit-product/{id}")
    public String showEditProductForm(@PathVariable Long id, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null || !(admin.getRole() == AdminRole.SUPER_ADMIN || admin.getRole() == AdminRole.PRODUCT_ADMIN)) {
            model.addAttribute("error", "You don't have permission to edit products.");
            return "admin_dashboard";
        }
        Book book = bookService.findBookById(id);
        if (book == null) {
            model.addAttribute("error", "Product not found.");
            return "admin_dashboard";
        }
        model.addAttribute("book", book);
        return "edit_book";
    }

    @PostMapping("/edit-product/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute("book") Book book, HttpSession session, Model model) {
        // Kiểm tra quyền admin
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null || !(admin.getRole() == AdminRole.SUPER_ADMIN || admin.getRole() == AdminRole.PRODUCT_ADMIN)) {
            model.addAttribute("error", "You don't have permission to edit products.");
            return "admin_dashboard";
        }

        // Tìm sản phẩm theo ID
        Book existingBook = bookService.findBookById(id);
        if (existingBook == null) {
            model.addAttribute("error", "Product not found.");
            return "admin_dashboard";
        }

        // Kiểm tra dữ liệu form
        if (book.getTitle() == null || book.getTitle().isEmpty() ||
                book.getAuthor() == null || book.getAuthor().isEmpty() ||
                book.getPrice() <= 0) {
            model.addAttribute("error", "Invalid input data. Please fill all required fields.");
            return "edit_book";
        }

        // Cập nhật thông tin sản phẩm
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setImage(book.getImage());
        existingBook.setPrice(book.getPrice());

        try {
            bookService.updateBook(existingBook);
            model.addAttribute("success", "Product updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while updating the product.");
            return "edit_book";
        }

        return "admin_dashboard";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null || !(admin.getRole() == AdminRole.SUPER_ADMIN || admin.getRole() == AdminRole.PRODUCT_ADMIN)) {
            model.addAttribute("error", "You don't have permission to delete products.");
            return "admin_dashboard";
        }
        Book book = bookService.findBookById(id);
        if (book == null) {
            model.addAttribute("error", "Product not found.");
            return "admin_dashboard";
        }
        bookService.deleteBookById(id);
        model.addAttribute("success", "Product deleted successfully!");
        return "admin_dashboard";
    }

    @GetMapping("/customers")
    public String viewCustomers(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");

        if (admin == null) {
            model.addAttribute("error", "You must log in to access this page.");
            return "admin_login";
        }

        if (admin.getRole() != AdminRole.SUPER_ADMIN && admin.getRole() != AdminRole.CUSTOMER_SUPPORT) {
            model.addAttribute("error", "You do not have permission to view customer list.");
            return "admin_dashboard";
        }

        List<Customers> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customer_list";
    }

    @GetMapping("/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
