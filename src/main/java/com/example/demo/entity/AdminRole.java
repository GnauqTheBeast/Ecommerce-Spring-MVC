package com.example.demo.entity;

public enum AdminRole {
    SUPER_ADMIN,       // Quản trị viên cấp cao nhất
    PRODUCT_ADMIN,     // Quản lý sản phẩm
    ORDER_ADMIN,       // Quản lý đơn hàng
    CUSTOMER_SUPPORT,  // Hỗ trợ khách hàng
    MARKETING_ADMIN,   // Quản lý marketing
    FINANCE_ADMIN;     // Quản lý tài chính

    @Override
    public String toString() {
        return name();
    }
}
