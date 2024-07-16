package com.example.blogproject.controller;

import com.example.blogproject.domain.Tag;
import com.example.blogproject.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.findAllTags();
    }


}
