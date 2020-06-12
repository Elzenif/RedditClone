package gso.training.reddit.service;

import gso.training.reddit.dto.SubredditDto;
import gso.training.reddit.model.Subreddit;
import gso.training.reddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        var subreddit = subredditRepository.save(mapSubredditDto(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
        return Subreddit.builder()
            .name(subredditDto.getName())
            .description(subredditDto.getDescription())
            .build();
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        return SubredditDto.builder()
            .id(subreddit.getId())
            .name(subreddit.getName())
            .numberOfPosts(subreddit.getPosts().size())
            .build();
    }

}
