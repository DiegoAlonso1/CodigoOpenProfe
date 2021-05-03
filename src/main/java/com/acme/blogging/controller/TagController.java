package com.acme.blogging.controller;

import com.acme.blogging.domain.model.Tag;
import com.acme.blogging.domain.service.TagService;
import com.acme.blogging.resource.SaveTagResource;
import com.acme.blogging.resource.TagResource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TagController {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public Page<TagResource> getAllTags(Pageable pageable) {
        List<TagResource> tags = tagService.getAllTags(pageable)
                .getContent().stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(tags, pageable, tags.size());
    }

    @GetMapping("/posts/{postId}/tags")
    public Page<TagResource> getAllTagsByPostId (@PathVariable Long postId, Pageable pageable) {
        List<TagResource> tags = tagService.getAllTagsByPostId(postId, pageable)
                .getContent().stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(tags, pageable, tags.size());
    }

    @GetMapping("/tags/{tagId}")
    public TagResource getTagById(@PathVariable Long tagId) {
        return convertToResource(tagService.getTagById(tagId));
    }

    @PostMapping("/tags")
    public  TagResource createTag(@Valid @RequestBody SaveTagResource resource) {
        return convertToResource(tagService.createTag(convertToEntity(resource)));
    }

    @PutMapping("/tags/{tagId}")
    public TagResource updateTag(@PathVariable Long tagId, @Valid @RequestBody SaveTagResource resource) {
        return convertToResource(tagService.updateTag(tagId, convertToEntity(resource)));
    }

    private Tag convertToEntity(SaveTagResource resource) {
        return mapper.map(resource, Tag.class);
    }

    private TagResource convertToResource(Tag entity) {
        return mapper.map(entity, TagResource.class);
    }
}
