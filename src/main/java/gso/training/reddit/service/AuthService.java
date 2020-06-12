package gso.training.reddit.service;

import gso.training.reddit.dto.AuthenticationResponse;
import gso.training.reddit.dto.LoginRequest;
import gso.training.reddit.dto.NotificationEmail;
import gso.training.reddit.dto.RegisterRequest;
import gso.training.reddit.exception.SpringRedditException;
import gso.training.reddit.exception.UsernameNotFoundException;
import gso.training.reddit.model.User;
import gso.training.reddit.model.VerificationToken;
import gso.training.reddit.repository.UserRepository;
import gso.training.reddit.repository.VerificationTokenRepository;
import gso.training.reddit.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        var user = createUser(registerRequest);
        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(createNotificationEmail(user, token));
    }

    private NotificationEmail createNotificationEmail(User user, String token) {
        return NotificationEmail.builder()
            .subject("Please activate your account")
            .recipient(user.getEmail())
            .body(
                "Thank you for signing up to Spring Reddit, please click on the below url to activate your account : " +
                    "http://localhost:8080/api/auth/accountVerification/" + token)
            .build();
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        var verificationToken = VerificationToken.builder()
            .token(token)
            .user(user)
            .build();
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    private User createUser(RegisterRequest registerRequest) {
        return User.builder()
            .username(registerRequest.getUsername())
            .email(registerRequest.getEmail())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .createdDate(Instant.now())
            .enabled(false)
            .build();
    }

    @Transactional
    public void verifyAccount(String token) {
        var verificationToken = verificationTokenRepository.findByToken(token)
            .orElseThrow(() -> new SpringRedditException("Invalid token"));
        fetchUserAndEnable(verificationToken);
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        var username = verificationToken.getUser().getUsername();
        var user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public AuthenticationResponse login(LoginRequest loginRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
            loginRequest.getPassword());
        var authenticate = authenticationManager
            .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        var token = jwtProvider.generateToken(authenticationToken);

        return new AuthenticationResponse(token, loginRequest.getUsername());
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        var username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
