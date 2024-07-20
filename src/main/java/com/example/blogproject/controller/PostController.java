package com.example.blogproject.controller;

import com.example.blogproject.domain.Post;
import com.example.blogproject.domain.Tag;
import com.example.blogproject.domain.User;
import com.example.blogproject.service.CommentService;
import com.example.blogproject.service.PostService;
import com.example.blogproject.service.TagService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final TagService tagService;
    private final CommentService commentService;

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
    public String createPost(@ModelAttribute Post post,
                             @RequestParam(value = "tags", required = false) String tagString,
                             @RequestParam(value = "action", required = false) String action,
                             HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        post.setUser(currentUser);

        // 태그 처리
        if (tagString != null && !tagString.isEmpty()) {
            List<Tag> tags = processTagString(tagString);
            post.setTags(tags);
        }

        boolean isDraft = "save_draft".equals(action);
        postService.savePost(post, isDraft);

        if (isDraft) {
            return "redirect:/drafts";
        } else {
            return "redirect:/";
        }
    }

    private List<Tag> processTagString(String tagString) {
        List<Tag> tags = new ArrayList<>();
        String[] tagNames = tagString.split(",");
        for (String tagName : tagNames) {
            tagName = tagName.trim();
            if (!tagName.isEmpty()) {
                Tag tag = tagService.findOrCreateTag(tagName);
                tags.add(tag);
            }
        }
        return tags;
    }

    @GetMapping("/drafts")
    public String listDrafts(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        List<Post> drafts = postService.getDraftsByUser(currentUser);
        model.addAttribute("drafts", drafts);
        return "drafts";
    }


    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        model.addAttribute("tagNames", String.join(", ", post.getTags().stream().map(Tag::getName).collect(Collectors.toList())));
        return "postForm";
    }

    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post, @RequestParam String tagNames, @RequestParam String action) {
        boolean publish = "publish".equals(action);
        postService.updatePost(id, post, tagNames, publish);
        return publish ? "redirect:/posts" : "redirect:/drafts";
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

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        try {
            Post post = postService.getPostById(id);
            model.addAttribute("post", post);
            model.addAttribute("tags", post.getTags());
            return "postDetail";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

}