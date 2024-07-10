package com.example.blogproject.repository;

import com.example.blogproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
//    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable); //최신순
//    List<Post> findAllByOrderByViewCountDesc(Pageable pageable); //조회수 순
//    List<Post> findAllByOrderByLikeCountDesc(Pageable pageable); //좋아요 순


}
