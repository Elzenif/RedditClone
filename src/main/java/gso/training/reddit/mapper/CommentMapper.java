package gso.training.reddit.mapper;

import gso.training.reddit.dto.CommentDto;
import gso.training.reddit.model.Comment;
import gso.training.reddit.model.Post;
import gso.training.reddit.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment map(CommentDto commentDto, Post post, User user);

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "username", source = "user.username")
    CommentDto mapToDto(Comment comment);
}
