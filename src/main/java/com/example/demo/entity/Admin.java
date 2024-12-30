package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    private String username;

    private String password;

    @Enumerated(EnumType.STRING) // Lưu vai trò dưới dạng chuỗi trong cơ sở dữ liệu
    private AdminRole role;

    // Getter và Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AdminRole getRole() {
        return role;
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }
}
