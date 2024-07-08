package com.example.blogproject.controller;

import com.example.blogproject.domain.User;
import com.example.blogproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class BlogController {
    @Autowired
    private UserService userService;

    @GetMapping("/@{username}")
    public String getUserBlog(@PathVariable String username, Model model, HttpSession session) {
        // 사용자 인증 확인
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");
        }

        // 요청된 사용자 정보 조회
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        model.addAttribute("user", user);
        return "blog";
    }
}
