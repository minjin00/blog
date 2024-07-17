package com.example.blogproject.service;

import com.example.blogproject.domain.Post;
import com.example.blogproject.domain.User;
import com.example.blogproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void savePost(Post post) {
        postRepository.save(post);
    }


    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Page<Post> getPostsPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAll(pageRequest);
    }

    public Page<Post> searchPosts(String searchType, String keyword, Pageable pageable) {
        switch (searchType) {
            case "username":
                return postRepository.findByUser_UsernameContaining(keyword, pageable);
            case "title":
                return postRepository.findByTitleContaining(keyword, pageable);
            case "content":
                return postRepository.findByContentContaining(keyword, pageable);
            default:
                throw new IllegalArgumentException("Invalid search type: " + searchType);
        }
    }



    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }




}
