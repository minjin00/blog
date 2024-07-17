package com.example.blogproject.controller;

import com.example.blogproject.domain.Blog;
import com.example.blogproject.domain.Post;
import com.example.blogproject.domain.Tag;
import com.example.blogproject.domain.User;
import com.example.blogproject.service.BlogService;
import com.example.blogproject.service.PostService;
import com.example.blogproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/blog/{username}")
    public String userPage(@PathVariable String username, Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || !sessionUser.getUsername().equals(username)) {
            return "redirect:/";
        }
        User user = userService.findByUsername(username);
        List<Post> userPosts = postService.getPostsByUser(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("posts", userPosts);
        return "blog";

    }


}