package gso.training.reddit.mapper;

import gso.training.reddit.dto.PostRequest;
import gso.training.reddit.dto.PostResponse;
import gso.training.reddit.model.Post;
import gso.training.reddit.model.Subreddit;
import gso.training.reddit.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "subreddit", source = "subreddit")
    Post mapRequestToPost(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapPostToResponse(Post post);
}
