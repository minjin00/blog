package com.example.blogproject.dto;

public class CheckResponse {
    private boolean exists;

    public CheckResponse(boolean exists) {
        this.exists = exists;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
