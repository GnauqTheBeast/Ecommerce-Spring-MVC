package com.example.demo.utils;

import com.example.demo.entity.Customers;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {

    public static Customers getLoggedInCustomer(HttpSession session) {
        return (Customers) session.getAttribute("customers");
    }
}