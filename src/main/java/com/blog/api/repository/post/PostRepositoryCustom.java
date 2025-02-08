package com.blog.api.repository.post;

import com.blog.api.domain.Post;
import com.blog.api.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
