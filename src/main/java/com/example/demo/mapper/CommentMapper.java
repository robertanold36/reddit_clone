package com.example.demo.mapper;

import com.example.demo.dto.CommentDto;
import com.example.demo.model.Comment;
import com.example.demo.model.Person;
import com.example.demo.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "createdDate",expression = "java(java.time.Instant.now())")
    @Mapping(target = "person",source = "person")
    @Mapping(target = "post",source = "post")
    Comment mapToComment(CommentDto commentDto, Post post, Person person);

    @Mapping(target = "userName",expression = "java(comment.getPerson().getUsername())")
    @Mapping(target ="postId",expression = "java(comment.getPost().getPostId())")
    CommentDto mapToDto(Comment comment);

}
