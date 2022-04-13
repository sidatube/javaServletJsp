package com.example.javaservletjsp.entity;

import com.example.javaservletjsp.annotation.Column;
import com.example.javaservletjsp.annotation.Id;
import com.example.javaservletjsp.annotation.Table;

@Table(name = "tbUser")
public class User {
    @Id
    @Column(name = "username", type = "VARCHAR(50)")
    private String username;
    @Column(name = "password", type = "VARCHAR(50)")
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

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
}
