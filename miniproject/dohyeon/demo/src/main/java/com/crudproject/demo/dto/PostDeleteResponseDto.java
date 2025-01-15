package com.crudproject.demo.dto;

public class PostDeleteResponseDto {
    //게시글 삭제시 성공/실패여부 반환용 dto
    private boolean success;

    public PostDeleteResponseDto(boolean success) {
        this.success = success;
    }
}
