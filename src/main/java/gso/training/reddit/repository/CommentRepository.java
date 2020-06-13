package gso.training.reddit.repository;

import gso.training.reddit.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c " +
        "FROM Comment c " +
        "INNER JOIN c.post p " +
        "WHERE p.id = :postId")
    List<Comment> findAllByPostId(@Param("postId") Long postId);

    @Query("SELECT c " +
        "FROM Comment c " +
        "INNER JOIN c.user u " +
        "WHERE u.username = :username")
    List<Comment> findAllByUsername(@Param("username") String username);
}
