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

    public Post savePost(Post post, boolean isDraft) {
        post.setDraft(isDraft);
        return postRepository.save(post);
    }


    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAllByIsDraftFalse(pageable);
    }

    public Page<Post> getPostsPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAllByIsDraftFalse(pageRequest);
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

    public List<Post> getDraftsByUser(User user) {
        return postRepository.findByUserAndIsDraftTrue(user);
    }


    public List<Post> getPublishedPostsByUser(User user) {
        return postRepository.findByUserAndIsDraftFalse(user);
    }


    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }


}
