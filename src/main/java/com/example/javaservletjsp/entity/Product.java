package com.example.javaservletjsp.entity;

import com.example.javaservletjsp.annotation.Column;
import com.example.javaservletjsp.annotation.Id;
import com.example.javaservletjsp.annotation.Table;

@Table(name = "tbProducts")
public class Product {
    @Id(AutoIncrement = true)
    @Column(name = "id", type = "INT")
    private int id;
    @Column(name = "name", type = "VARCHAR(250)")
    private String name;
    @Column(name = "price", type = "DOUBLE")
    private double price;
    @Column(name = "amount", type = "INT")
    private int amount;
    @Column(name = "details", type = "TEXT")
    private String details;

    public Product() {
    }

    public Product(String name, double price, int amount, String details) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.details = details;
    }

    public Product(int id, String name, double price, int amount, String details) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
