package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Subreddit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "subreddit name is required")
    @Column(unique = true)
    private String name;

    @NotBlank(message = "description is required")
    private String description;

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE,CascadeType.REFRESH})
    private List<Post> posts;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    private Instant createdDate;
}
