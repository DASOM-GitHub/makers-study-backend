package com.crudproject.demo.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Post extends AutoTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //기본키 자동생성
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    public Post(String title, String content, String author) {
        super();
        this.title = title;
        this.content = content;
        this.author = author;
    }
}