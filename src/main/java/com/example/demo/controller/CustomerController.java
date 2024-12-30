package com.example.demo.controller;

import com.example.demo.entity.Customers;
import com.example.demo.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("customers", new Customers());
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("customers", new Customers());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("customers") Customers customers, Model model) {
        if (customers.getPassword() == null || customers.getPassword().isEmpty()) {
            model.addAttribute("error", "Password is required");
            return "register";
        }
        if (customerService.existsByUsername(customers.getUsername())) {
            model.addAttribute("error", "Username already exists. Please choose a different one.");
            return "register";
        }
        customers.setRegistration_date(LocalDate.now());
        customerService.saveCustomer(customers);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("customers") Customers customers, Model model, HttpSession session) {
        boolean isAuthenticated = customerService.authenticateCustomer(customers.getUsername(), customers.getPassword());
        if (isAuthenticated) {
            Customers authenticatedCustomers = customerService.findByCustomer(customers.getUsername());
            session.setAttribute("customers", authenticatedCustomers);
            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/dashboard";
    }

    @GetMapping("/customer")
    public String getAllCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customerList";
    }

    @GetMapping("/customer/{id}")
    public String getCustomerById(@PathVariable String id, Model model) {
        model.addAttribute("customers", customerService.getCustomerById(id));
        return "customerDetails";
    }

    @GetMapping("/customer/new")
    public String createCustomerForm(Model model) {
        model.addAttribute("customers", new Customers());
        return "customerForm";
    }

    @PostMapping("/customer")
    public String saveCustomer(@ModelAttribute("customers") Customers customers) {
        customerService.saveCustomer(customers);
        return "redirect:/customer";
    }
}
