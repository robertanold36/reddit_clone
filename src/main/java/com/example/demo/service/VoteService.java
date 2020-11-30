package com.example.demo.service;


import com.example.demo.dto.VoteDto;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.exception.SpringRedditException;
import com.example.demo.model.Post;
import com.example.demo.model.Vote;
import com.example.demo.model.VoteType;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final AuthService authService;
    private final PostRepository postRepository;

    @Transactional
    public void vote(VoteDto voteDto){
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("post with id " + voteDto.getPostId() + " " +
                        "not found"));
        Optional<Vote> voteByPostAndPerson = voteRepository
                .findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if(voteByPostAndPerson.isPresent()&&
                voteByPostAndPerson.get().getVoteType()
                        .equals(voteDto.getVoteType())){
            throw  new
                    SpringRedditException("you have already vote this " +
                    "kind of post maybe try to change your voteType");
        }

        if(VoteType.UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount()+1);
        }
        else {
            post.setVoteCount(post.getVoteCount()-1);

        }

        voteRepository.save(mapToVote(voteDto,post));
        postRepository.save(post);
    }


    private Vote mapToVote(VoteDto voteDto, Post post){
        return Vote.builder()
                .post(post)
                .voteType(voteDto.getVoteType())
                .user(authService.getCurrentUser())
                .build();
    }
}
