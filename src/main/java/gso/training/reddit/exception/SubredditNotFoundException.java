package gso.training.reddit.exception;

public class SubredditNotFoundException extends SpringRedditException {

    public SubredditNotFoundException(String subredditName) {
        super("No subreddit found with name: " + subredditName);
    }

    public SubredditNotFoundException(Long id) {
        super("No subreddit found with id: " + id);
    }
}
