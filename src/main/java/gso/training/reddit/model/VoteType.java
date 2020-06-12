package gso.training.reddit.model;

import lombok.Getter;

@Getter
public enum VoteType {

    UPVOTE(1), DOWNVOTE(-1);

    private final int direction;

    VoteType(int direction) {
        this.direction = direction;
    }
}
