package com.example.demo.mapper;

import com.example.demo.dto.PostRequestDto;
import com.example.demo.dto.PostResponse;
import com.example.demo.model.Person;
import com.example.demo.model.Post;
import com.example.demo.model.Subreddit;
import com.example.demo.repository.CommentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;

    @Mapping(target = "createdDate",expression = "java(java.time.Instant.now())")
    @Mapping(target = "description",source = "postRequestDto.description")
    @Mapping(target = "person",source = "person")
    @Mapping(target = "subreddit",source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post mapToPost(PostRequestDto postRequestDto, Subreddit subreddit, Person person);


    @Mapping(target = "id",source = "post.postId")
    @Mapping(target = "subredditName",source = "post.subreddit.name")
    @Mapping(target = "userName",source = "post.person.username")
    @Mapping(target = "description",source = "post.description")
    @Mapping(target = "commentCount",expression = "java(commentCount(post))")
    public abstract PostResponse mapToDto(Post post);


    Integer commentCount(Post post){
        return commentRepository.findByPost(post).size();
    }
}
