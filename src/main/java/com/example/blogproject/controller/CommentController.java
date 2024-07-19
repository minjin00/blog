package com.example.blogproject.controller;

import com.example.blogproject.domain.Comment;
import com.example.blogproject.domain.Post;
import com.example.blogproject.domain.User;
import com.example.blogproject.service.CommentService;
import com.example.blogproject.service.PostService;
import com.example.blogproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

//    @PostMapping("/{postId}/comment")
//    public String addComment(@RequestParam("postId") Long postId, @RequestParam("content") String content,
//                             HttpSession session) {
//
//    }




}
