package com.example.blogproject.repository;

import com.example.blogproject.domain.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findByUserUsername(String username);

}
