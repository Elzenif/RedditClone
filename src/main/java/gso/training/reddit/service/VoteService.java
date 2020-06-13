package gso.training.reddit.service;

import gso.training.reddit.dto.VoteDto;
import gso.training.reddit.exception.AlreadyVotedException;
import gso.training.reddit.exception.PostNotFoundException;
import gso.training.reddit.model.Post;
import gso.training.reddit.model.User;
import gso.training.reddit.model.Vote;
import gso.training.reddit.repository.PostRepository;
import gso.training.reddit.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final AuthService authService;
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public void vote(VoteDto voteDto) {
        var post = postRepository.findById(voteDto.getPostId())
            .orElseThrow(() -> new PostNotFoundException(voteDto.getPostId()));
        var user = authService.getCurrentUser();
        var optionalVote = checkIfAlreadyVoted(voteDto, post, user);

        if (optionalVote.isPresent()) {
            reverseVote(post, voteDto);
            optionalVote.get().setType(voteDto.getType());
        } else {
            countVote(post, voteDto);
            voteRepository.save(map(voteDto, post, user));
        }
    }

    private Vote map(VoteDto voteDto, Post post, User user) {
        return Vote.builder()
            .type(voteDto.getType())
            .post(post)
            .user(user)
            .build();
    }

    private void reverseVote(Post post, VoteDto voteDto) {
        post.setVoteCount(post.getVoteCount() + voteDto.getType().getDirection() * 2);
    }

    private void countVote(Post post, VoteDto voteDto) {
        post.setVoteCount(post.getVoteCount() + voteDto.getType().getDirection());
    }

    private Optional<Vote> checkIfAlreadyVoted(VoteDto voteDto, Post post, User user) {
        var optional = voteRepository.findByPostAndUser(post, user);
        if (optional.isPresent() && optional.get().getType() == voteDto.getType()) {
            throw new AlreadyVotedException(post, user, voteDto);
        }
        return optional;
    }
}
