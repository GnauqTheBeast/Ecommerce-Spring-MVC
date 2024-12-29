package com.example.demo.entity;

import jakarta.persistence.Entity;

@Entity
public class Book extends Product {
    private String title;
    private String author;
    private String image;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
