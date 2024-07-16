package com.example.blogproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "series")
@Getter @Setter
@NoArgsConstructor
public class Series {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seriesName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<Post> posts;
}
