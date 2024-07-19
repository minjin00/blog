package com.example.blogproject.service;

import com.example.blogproject.domain.Comment;
import com.example.blogproject.domain.Post;
import com.example.blogproject.domain.User;
import com.example.blogproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment addComment(Post post, User user, String content) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByContent(postId);
    }


}
