package dmu.dasom.miniproject.dto;

import dmu.dasom.miniproject.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime registerDate;
    private LocalDateTime updateDate;

    public static PostDto fromEntity(Post post){
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .registerDate(post.getRegisterDate())
                .updateDate(post.getUpdateDate())
                .build();
    }

    public Post toEntity(){
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
