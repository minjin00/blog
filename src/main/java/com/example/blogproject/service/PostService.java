package com.example.blogproject.service;

import com.example.blogproject.domain.Post;
import com.example.blogproject.domain.Tag;
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
    private final TagService tagService;

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

    @Transactional
    public void updatePost(Long id, Post updatedPost, String tagNames, boolean publish) {
        Post post = getPostById(id);
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setPublished(publish);

//        List<Tag> tags = tagService.getOrCreateTags(tagNames);
//        post.setTags(tags);

        postRepository.save(post);
    }


}
