package gso.training.reddit.exception;

public class PostNotFoundException extends SpringRedditException {

    public PostNotFoundException(Long id) {
        super("No post found with id: " + id);
    }
}
