package com.example.demo.repository;

import com.example.demo.entity.Admin;
import com.example.demo.entity.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    Admin findByUsername(String username);

    List<Admin> findByRole(AdminRole role); // Lấy danh sách admin theo vai trò
}
