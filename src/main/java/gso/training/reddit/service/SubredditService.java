package gso.training.reddit.service;

import gso.training.reddit.dto.SubredditDto;
import gso.training.reddit.exception.SpringRedditException;
import gso.training.reddit.mapper.SubredditMapper;
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
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        var subreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
            .stream()
            .map(subredditMapper::mapSubredditToDto)
            .collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id) {
        var subreddit = subredditRepository.findById(id)
            .orElseThrow(() -> new SpringRedditException("No subreddit found with id: " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
