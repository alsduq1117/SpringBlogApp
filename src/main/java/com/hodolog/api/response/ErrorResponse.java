package com.hodolog.api.response;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;
    private Map<String, String> validation = new HashMap<>();

    public void addValidation(String field, String errorMessage){
        this.validation.put(field,errorMessage);
    }




}
