package com.assignment.user.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class Response {

    private String message;
    private boolean success;
    private Object data;

}
