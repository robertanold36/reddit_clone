package com.example.demo.repository;

import com.example.demo.model.Person;
import com.example.demo.model.Post;
import com.example.demo.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findBySubreddit(Subreddit subreddit_not_found);

    List<Post> findByPerson(Person person);
}
