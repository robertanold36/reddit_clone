package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class PostRequestDto {

    private String description;
    private String postName;
    private Instant createdDate;
    private String url;
    private String subredditName;
}
