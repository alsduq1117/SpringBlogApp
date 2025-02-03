package com.blog.api.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Signup {

    private String name;
    private String password;
    private String email;

}
