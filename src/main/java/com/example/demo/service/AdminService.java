package com.example.demo.service;

import com.example.demo.entity.Admin;
import com.example.demo.entity.AdminRole;
import com.example.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public boolean authenticateAdmin(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        return admin != null && admin.getPassword().equals(password);
    }

    public Admin findByAdminUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public List<Admin> findAdminsByRole(AdminRole role) {
        return adminRepository.findByRole(role);
    }
}
