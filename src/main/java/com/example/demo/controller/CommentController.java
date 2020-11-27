package com.example.demo.controller;

import com.example.demo.dto.CommentDto;
import com.example.demo.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment/")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> saveComment(@RequestBody CommentDto commentDto){
        commentService.saveComment(commentDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentByPost(@PathVariable Long postId){
        List<CommentDto> allCommentsByPost = commentService.getAllCommentsByPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(allCommentsByPost);
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentDto>> getCommentByUsername(@PathVariable String userName){
        List<CommentDto> allCommentsByPerson = commentService.getAllCommentsByPerson(userName);
        return  ResponseEntity.status(HttpStatus.OK).body(allCommentsByPerson);
    }

}
