package com.example.demo.model;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Lob
    @Nullable
    private String description;

    @NotBlank(message = "post name must not blank")
    private String postName;

    private Instant createdDate;

    @Nullable
    private String url;

    private Integer voteCount=0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personId" ,referencedColumnName = "id")
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subredditId" ,referencedColumnName = "id")
    private Subreddit subreddit;
}
