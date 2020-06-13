package gso.training.reddit.service;

import gso.training.reddit.dto.CommentDto;
import gso.training.reddit.dto.NotificationEmail;
import gso.training.reddit.exception.PostNotFoundException;
import gso.training.reddit.mapper.CommentMapper;
import gso.training.reddit.model.User;
import gso.training.reddit.repository.CommentRepository;
import gso.training.reddit.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private static final String POST_URL = "";
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    @Transactional
    public void save(CommentDto commentDto) {
        var post = postRepository.findById(commentDto.getPostId())
            .orElseThrow(() -> new PostNotFoundException(commentDto.getPostId()));
        var user = authService.getCurrentUser();
        commentRepository.save(commentMapper.map(commentDto, post, user));

        var message = mailContentBuilder.build(post.getUser().getUsername() +
            " posted a comment on your post: " + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        var notificationEmail = new NotificationEmail(user.getUsername() + " commented on your post",
            user.getEmail(), message);
        mailService.sendMail(notificationEmail);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsForPost(Long postId) {
        return commentRepository.findAllByPostId(postId)
            .stream()
            .map(commentMapper::mapToDto)
            .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByUsername(String username) {
        return commentRepository.findAllByUsername(username)
            .stream()
            .map(commentMapper::mapToDto)
            .collect(toList());
    }

}
