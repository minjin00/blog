package com.example.blogproject.controller;

import com.example.blogproject.domain.Post;
import com.example.blogproject.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private PostService postService;



    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "main";
    }

    @GetMapping("/blog")
    public String blog(HttpSession session) {
        if(session.getAttribute("user") == null) {
            return "redirect;/loginform";
        }
        return "blog";
    }
}
