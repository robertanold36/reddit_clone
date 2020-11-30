package com.example.demo.repository;

import com.example.demo.model.Person;
import com.example.demo.model.Post;
import com.example.demo.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, Person currentUser);
}
