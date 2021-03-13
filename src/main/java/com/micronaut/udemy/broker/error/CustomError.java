package com.micronaut.udemy.broker.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomError {
    private int status;
    private String error;
    private String message;
    private String path;
}
