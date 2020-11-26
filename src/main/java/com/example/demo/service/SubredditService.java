package com.example.demo.service;

import com.example.demo.dto.SubredditDto;
import com.example.demo.exception.SubredditNotFoundException;
import com.example.demo.mapper.MapperSubreddit;
import com.example.demo.model.Subreddit;
import com.example.demo.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final MapperSubreddit subredditMapper;

    public SubredditDto save(SubredditDto subredditDto){

        Subreddit save = subredditRepository.save(subredditMapper.mapToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    public List<SubredditDto> getAllSubreddit(){
        return subredditRepository.findAll().stream()
                .map(subredditMapper::mapToSubredditDto)
                .collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id){
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() ->
                new SubredditNotFoundException("subreddit with id " + id + " not found"));

        return subredditMapper.mapToSubredditDto(subreddit);
    }
}
