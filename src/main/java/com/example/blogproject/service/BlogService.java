package com.example.blogproject.service;

import com.example.blogproject.domain.Blog;
import com.example.blogproject.domain.Post;
import com.example.blogproject.domain.Tag;
import com.example.blogproject.domain.User;
import com.example.blogproject.repository.BlogRepository;
import com.example.blogproject.repository.PostRepository;
import com.example.blogproject.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public Blog createBlog(User user) {
        Blog blog = new Blog();
        blog.setUser(user);
        blog.setTitle(user.getUsername() + "'s Blog");
        return blogRepository.save(blog);
    }

    public Blog getBlogByUsername(String username) {
        return blogRepository.findByUserUsername(username).orElseThrow(() ->
                new RuntimeException("블로그를 찾을 수 없습니다."));
    }


}
