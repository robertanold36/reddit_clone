package com.example.demo.service;


import com.example.demo.dto.PostRequestDto;
import com.example.demo.dto.PostResponse;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.exception.SubredditNotFoundException;
import com.example.demo.mapper.PostMapper;
import com.example.demo.model.Person;
import com.example.demo.model.Post;
import com.example.demo.model.Subreddit;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final SubredditRepository subredditRepository;
    private final PersonRepository personRepository;
    private final AuthService authService;

    public void savePost(PostRequestDto postRequestDto){

        Subreddit bySubredditName = subredditRepository.findByName(postRequestDto.getSubredditName())
                .orElseThrow(()->
                new SubredditNotFoundException("subreddit not found"));
        Person person=authService.getCurrentUser();

        Post post = postMapper.mapToPost(postRequestDto, bySubredditName, person);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(){
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("post not found"));

        return postMapper.mapToDto(post);

    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostBySubreddit(Long id){
        Subreddit subreddit_not_found = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("subreddit not found"));
        return postRepository.findBySubreddit(subreddit_not_found)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostByUsername(String username){
        Person person = personRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
       return postRepository.findByPerson(person)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());

    }

}
