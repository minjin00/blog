package com.example.blogproject.controller;

import com.example.blogproject.domain.Blog;
import com.example.blogproject.domain.Post;
import com.example.blogproject.domain.Tag;
import com.example.blogproject.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

//    @GetMapping("/")
//    public String home(Model model,
//                       @RequestParam(defaultValue = "recent") String sort,
//                       @RequestParam(defaultValue = "0") int page) {
//        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("createdAt").descending());
//        Page<Post> posts;
//
//        model.addAttribute("posts", posts);
//        model.addAttribute("sort", sort);
//        return "home";
//    }


}