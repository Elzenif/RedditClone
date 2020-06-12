package gso.training.reddit.service;

import gso.training.reddit.dto.PostRequest;
import gso.training.reddit.dto.PostResponse;
import gso.training.reddit.exception.PostNotFoundException;
import gso.training.reddit.exception.SubredditNotFoundException;
import gso.training.reddit.mapper.PostMapper;
import gso.training.reddit.model.Post;
import gso.training.reddit.repository.PostRepository;
import gso.training.reddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final AuthService authService;
    private final SubredditRepository subredditRepository;
    private final PostMapper postMapper;

    @Transactional
    public Post save(PostRequest postRequest) {
        var subreddit = subredditRepository.findByName(postRequest.getSubredditName())
            .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        var user = authService.getCurrentUser();

        return postRepository.save(postMapper.mapRequestToPost(postRequest, subreddit, user));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        var post = postRepository.findById(id)
            .orElseThrow(() -> new PostNotFoundException(id));
        return postMapper.mapPostToResponse(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
            .stream()
            .map(postMapper::mapPostToResponse)
            .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        return postRepository.findBySubreddit(subredditId)
            .stream()
            .map(postMapper::mapPostToResponse)
            .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        return postRepository.findByUsername(username)
            .stream()
            .map(postMapper::mapPostToResponse)
            .collect(toList());
    }
}
