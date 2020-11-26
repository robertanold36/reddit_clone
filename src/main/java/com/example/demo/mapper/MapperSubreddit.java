package com.example.demo.mapper;


import com.example.demo.dto.SubredditDto;
import com.example.demo.model.Post;
import com.example.demo.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperSubreddit {


    @Mapping(target = "numberOfPosts",expression = "java(numberOfPosts(subreddit.getPosts()))")
    SubredditDto mapToSubredditDto(Subreddit subreddit);

    default Integer numberOfPosts(List<Post> posts){return posts.size();}

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapToSubreddit(SubredditDto subredditDto);
}
