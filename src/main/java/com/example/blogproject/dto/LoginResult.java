package com.example.blogproject.dto;

import com.example.blogproject.domain.User;

public class LoginResult {
    private boolean success;
    private String message;
    private User user;

    public LoginResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.user = null;
    }

    public LoginResult(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}