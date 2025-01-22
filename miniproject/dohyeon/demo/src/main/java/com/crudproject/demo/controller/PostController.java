package com.crudproject.demo.controller;

import com.crudproject.demo.dto.PostDeleteResponseDto;
import com.crudproject.demo.dto.PostRequestDto;
import com.crudproject.demo.dto.PostResponseDto;
import com.crudproject.demo.entity.Post;
import com.crudproject.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //전체조회
    @GetMapping("/api/posts")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    //개별조회
    @GetMapping("/api/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    //게시글수정
    @PutMapping("/api/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
        return postService.updatePost(id, postRequestDto);
    }

    //게시글작성
    @PostMapping("/api/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }

    //게시글삭제
    @DeleteMapping("/api/post/{id}")
    public PostDeleteResponseDto deletePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
        return postService.deletePost(id, postRequestDto);
    }
}
