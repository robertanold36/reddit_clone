package com.example.demo.controller;


import com.example.demo.dto.SubredditDto;
import com.example.demo.service.SubredditService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> saveDto(@RequestBody SubredditDto subredditDto){

        subredditService.save(subredditDto);
        return new ResponseEntity<>(subredditDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddit(){
        List<SubredditDto> allSubreddit = subredditService.getAllSubreddit();
        return new ResponseEntity<>(allSubreddit,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable("id") Long id){
        SubredditDto subreddit = subredditService.getSubreddit(id);
        return new ResponseEntity<>(subreddit,HttpStatus.OK);
    }
}
