package gso.training.reddit.exception;

public class UsernameNotFoundException extends SpringRedditException {

    public UsernameNotFoundException(String username) {
        super("No user found with username: " + username);
    }
}
