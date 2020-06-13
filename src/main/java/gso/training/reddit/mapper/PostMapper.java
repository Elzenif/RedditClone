package gso.training.reddit.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import gso.training.reddit.dto.PostRequest;
import gso.training.reddit.dto.PostResponse;
import gso.training.reddit.model.Post;
import gso.training.reddit.model.Subreddit;
import gso.training.reddit.model.User;
import gso.training.reddit.repository.CommentRepository;
import gso.training.reddit.repository.VoteRepository;
import gso.training.reddit.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "subreddit", source = "subreddit")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.countByPost(post);
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
}
