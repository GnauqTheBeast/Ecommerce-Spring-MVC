package com.example.demo.service;

import com.example.demo.entity.Customers;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customers findByCustomer(String username) {
        return customerRepository.findByUsername(username);
    }

    public boolean authenticateCustomer(String username, String password) {
        Customers customers = findByCustomer(username);
        return customers != null && customers.getPassword().equals(password);
    }

    public Boolean existsByUsername(String username) {
        Customers customers = customerRepository.findByUsername(username);
        if (customers == null) return false;
        return true;
    }

    public List<Customers> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customers getCustomerById(String id) {
        return customerRepository.findById(id).orElse(null);
    }

    public void saveCustomer(Customers customers) {
        customerRepository.save(customers);
    }

    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }
}
