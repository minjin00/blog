package com.example.blogproject.service;

import com.example.blogproject.domain.Tag;
import com.example.blogproject.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    public Tag saveTag(Tag tag) {
        Optional<Tag> existingTag = tagRepository.findByName(tag.getName());
        if(existingTag.isPresent()) {
            return existingTag.get();
        }

        return tagRepository.save(tag);
    }

    public Tag findOrCreateTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setName(name);
                    return tagRepository.save(newTag);
                });
    }

}
