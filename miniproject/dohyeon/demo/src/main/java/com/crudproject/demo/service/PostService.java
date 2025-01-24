package com.crudproject.demo.service;

import com.crudproject.demo.dto.PostDeleteResponseDto;
import com.crudproject.demo.dto.PostRequestDto;
import com.crudproject.demo.dto.PostResponseDto;
import com.crudproject.demo.entity.Post;
import com.crudproject.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    //게시글 전체조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    //게시글 개별조회
    @Transactional
    public PostResponseDto getPost(Long id) {
        return postRepository.findById(id).map(PostResponseDto::new).orElse(null);
    }

    //게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("아이디 존재 x"));

        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        postRepository.save(post);

        return new PostResponseDto(post);
    }

    //게시글 작성
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        Post post = new Post(
                postRequestDto.getTitle(),
                postRequestDto.getContent(),
                postRequestDto.getAuthor()
        );
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    //게시글 삭제
    @Transactional
    public PostDeleteResponseDto deletePost(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디 존재 x")
        );
        postRepository.delete(post);
        return new PostDeleteResponseDto(true);
    }
}
