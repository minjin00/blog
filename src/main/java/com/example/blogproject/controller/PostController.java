package com.example.blogproject.controller;

import com.example.blogproject.domain.Comment;
import com.example.blogproject.domain.Post;
import com.example.blogproject.domain.Tag;
import com.example.blogproject.domain.User;
import com.example.blogproject.service.CommentService;
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
                             @RequestParam(value = "action", required = false) String action,
                             HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        post.setUser(currentUser);

        boolean isDraft = "save_draft".equals(action);
        postService.savePost(post, isDraft);

        if (isDraft) {
            return "redirect:/drafts";
        } else {
            return "redirect:/";
        }
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

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        Post post = postService.getPostById(id);

        if (currentUser == null || !post.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/";
        }

        model.addAttribute("post", post);
        return "editform";
    }

    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable Long id,
                             @ModelAttribute Post updatedPost,
                             @RequestParam(value = "action", required = false) String action,
                             HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        Post existingPost = postService.getPostById(id);

        if (currentUser == null || !existingPost.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/";
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setTags(updatedPost.getTags());

        boolean isDraft = "save_draft".equals(action);
        postService.savePost(existingPost, isDraft);

        if (isDraft) {
            return "redirect:/drafts";
        } else {
            return "redirect:/post/" + id;
        }
    }

    @GetMapping("/edit/{index}")
    public String editDraft(@PathVariable int index, Model model, HttpSession session) {
        List<Post> drafts = (List<Post>) session.getAttribute("drafts");
        if (drafts != null && index >= 0 && index < drafts.size()) {
            Post draft = drafts.get(index);
            model.addAttribute("post", draft);
            model.addAttribute("draftIndex", index);
            return "postform";
        }
        return "redirect:/drafts";
    }

    @PostMapping("/edit/{index}")
    public String updateDraft(@PathVariable int index,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("tags") String tags,
                              @RequestParam(value = "action", required = false) String action,
                              HttpSession session) {
        List<Post> drafts = (List<Post>) session.getAttribute("drafts");
        if (drafts != null && index >= 0 && index < drafts.size()) {
            Post draft = drafts.get(index);
            draft.setTitle(title);
            draft.setContent(content);
            draft.setTags(processTagString(tags));

            if ("publish".equals(action)) {
                // 출간하기
                draft.setDraft(false);
                //postService.savePost(post);
                drafts.remove(index);
                session.setAttribute("drafts", drafts);
                return "redirect:/";
            } else {
                // 임시저장 업데이트
                session.setAttribute("drafts", drafts);
                return "redirect:/drafts";
            }
        }
        return "redirect:/drafts";
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