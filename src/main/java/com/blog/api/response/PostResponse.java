package com.blog.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0,Math.min(title.length(), 10));
        this.content = content;
    }

//    //어플리케이션 정책을 응답클래스를 분리하여 집어넣어주는게 포인트
//    public String getTitle() {
//        return this.title.substring(0,10);
//    }
}
