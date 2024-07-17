package com.example.blogproject.repository;

import com.example.blogproject.domain.Post;
import com.example.blogproject.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    Page<Post> findByUser_UsernameContaining(String username, Pageable pageable); //이름 검색
    Page<Post> findByTitleContaining(String title, Pageable pageable); //제목 검색
    Page<Post> findByContentContaining(String content, Pageable pageable); //내용 검색

}
