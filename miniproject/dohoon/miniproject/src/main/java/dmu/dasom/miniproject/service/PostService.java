package dmu.dasom.miniproject.service;

import dmu.dasom.miniproject.domain.Post;
import dmu.dasom.miniproject.dto.PostDto;
import dmu.dasom.miniproject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostDto> getAllPost(){
        return postRepository.findAll()
                .stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }

    public PostDto getPostById(Long id){
        return postRepository.findById(id)
                .map(PostDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public PostDto createPost(PostDto postDto){
        Post post = postDto.toEntity();
        return PostDto.fromEntity(postRepository.save(post));
    }

    public PostDto updatePost(Long id, PostDto postDto){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUpdateDate(LocalDateTime.now());
        postRepository.save(post);

        return PostDto.fromEntity(post);
    }

    public void deletePost(Long id){
        if(!postRepository.existsById(id)){
            throw new RuntimeException("Post not found");
        } else {
            postRepository.deleteById(id);
        }
    }
}
