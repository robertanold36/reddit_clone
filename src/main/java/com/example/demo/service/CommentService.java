package com.example.demo.service;

import com.example.demo.dto.CommentDto;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.model.Person;
import com.example.demo.model.Post;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final PersonRepository personRepository;

    public void saveComment(CommentDto commentDto){
        Long postId=commentDto.getPostId();
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new PostNotFoundException("post with id " + postId + " not found"));
        Person currentUser = authService.getCurrentUser();
        commentRepository.save(commentMapper.mapToComment(commentDto,post,currentUser));
    }

    public List<CommentDto> getAllCommentsByPost(Long postId){
        Post post=postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException("post with id "+postId+" not found"));
       return commentRepository.findByPost(post).stream()
                .map(commentMapper::mapToDto).collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsByPerson(String username){
        Person person = personRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user with username " + username + " not found"));
        return commentRepository.findByPerson(person)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

}
