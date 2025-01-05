package com.example.textile.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
public class ErrorResponseDto {
    private HttpStatus httpStatus;
    private Map<String, String> errorMessages;
    private LocalDateTime errorDateTime;
}
