package com.blog.api.controller;

import com.blog.api.request.comment.CommentCreate;
import com.blog.api.request.comment.CommentDelete;
import com.blog.api.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public void write(@PathVariable(name = "postId") Long postId, @RequestBody @Valid CommentCreate request) {
        commentService.write(postId, request);
    }

    @PostMapping("/comments/{commentId}/delete")
    public void delete(@PathVariable(name = "commentId") Long commentId, @RequestBody @Valid CommentDelete request) {
        commentService.delete(commentId, request);
    }
}
