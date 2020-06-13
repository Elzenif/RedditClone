package gso.training.reddit.repository;

import gso.training.reddit.model.Post;
import gso.training.reddit.model.User;
import gso.training.reddit.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByPostAndUser(Post post, User user);
}
