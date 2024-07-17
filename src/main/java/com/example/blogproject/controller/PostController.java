package com.example.blogproject.controller;

import com.example.blogproject.domain.Post;
import com.example.blogproject.domain.Tag;
import com.example.blogproject.domain.User;
import com.example.blogproject.service.PostService;
import com.example.blogproject.service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final TagService tagService;

    @GetMapping("/posts")
    public String listPosts(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "15") int size) {
        Page<Post> postPage = postService.getPostsPage(page, size);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        return "main";
    }

    @GetMapping("/write")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "postform";
    }

    @PostMapping("/write")
    public String createPost(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("tags") String tags,
                             HttpServletRequest request) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        // 태그 처리
        Set<Tag> tagSet = processTagString(tags);
        post.setTags(tagSet);

        // 현재 로그인한 사용자 정보 설정
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        post.setUser(currentUser);

        // 게시글 저장
        postService.savePost(post);

        return "redirect:/";
    }

    private Set<Tag> processTagString(String tags) {
        Set<Tag> tagSet = new HashSet<>();
        if (tags != null && !tags.isEmpty()) {
            String[] tagArray = tags.split("\\s+");
            for (String tagName : tagArray) {
                if (!tagName.trim().isEmpty()) {
                    Tag tag = tagService.findOrCreateTag(tagName.trim());
                    tagSet.add(tag);
                }
            }
        }
        return tagSet;
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam String searchType,
                              @RequestParam String keyword,
                              @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                              Model model) {
        Page<Post> posts = postService.searchPosts(searchType, keyword, pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        return "main";
    }





}