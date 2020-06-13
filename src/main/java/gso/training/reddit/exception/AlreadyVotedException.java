package gso.training.reddit.exception;

import gso.training.reddit.dto.VoteDto;
import gso.training.reddit.model.Post;
import gso.training.reddit.model.User;

public class AlreadyVotedException extends SpringRedditException {

    public AlreadyVotedException(Post post, User user, VoteDto voteDto) {
        super("User " + user.getUsername() + " already " + voteDto.getType() + "d for the post " + post.getId());
    }
}
