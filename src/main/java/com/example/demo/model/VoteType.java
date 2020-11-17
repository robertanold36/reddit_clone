package com.example.demo.model;

public enum VoteType {

    UPVOTE(1), DOWN_VOTE(-1),
    ;

    private int direction;

    VoteType(int direction) {
    }

    public Integer getDirection() {
        return direction;
    }
}
