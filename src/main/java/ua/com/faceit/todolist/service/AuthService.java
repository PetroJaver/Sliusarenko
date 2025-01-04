package ua.com.faceit.todolist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.faceit.todolist.data.ERole;
import ua.com.faceit.todolist.data.RefreshToken;
import ua.com.faceit.todolist.data.Role;
import ua.com.faceit.todolist.data.User;
import ua.com.faceit.todolist.data.payload.request.LoginRequest;
import ua.com.faceit.todolist.data.payload.request.SignupRequest;
import ua.com.faceit.todolist.data.payload.request.TokenRefreshRequest;
import ua.com.faceit.todolist.data.payload.response.JwtResponse;
import ua.com.faceit.todolist.data.payload.response.TokenRefreshResponse;
import ua.com.faceit.todolist.exception.BadRequestException;
import ua.com.faceit.todolist.exception.SignInException;
import ua.com.faceit.todolist.exception.TokenRefreshException;
import ua.com.faceit.todolist.jwt.JwtUtils;
import ua.com.faceit.todolist.repository.RoleRepository;
import ua.com.faceit.todolist.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    @Value("${webServerUrl}")
    private String webServerUrl;

    private final EmailService emailService;

    public void signup(SignupRequest signupRequest) {
        String firstName = signupRequest.getFirstName();
        String lastName = signupRequest.getLastName();
        String email = signupRequest.getEmail();
        String username = signupRequest.getUsername();
        String password = signupRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Error: Email is already in use!");
        }

        User user = new User(username, encodedPassword, email, firstName, lastName);

        // Disable user until they click on confirmation link in email
        user.setEnabled(false);

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role role = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new BadRequestException("Error: Role not found!"));
            roles.add(role);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);

        // Generate random 36-character string token for confirmation link
        user.setConfirmationToken(UUID.randomUUID().toString());

        userRepository.save(user);

        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(user.getEmail());
        registrationEmail.setSubject("Registration Confirmation");
        registrationEmail.setText("To confirm your e-mail address, please click the link below:\n" + webServerUrl
                + "/api/auth/confirm?token=" + user.getConfirmationToken());
        registrationEmail.setFrom("noreply@domain.com");

        emailService.sendEmail(registrationEmail);
    }

    public JwtResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return new JwtResponse(
                    jwt,
                    refreshToken.getToken(),
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles);

        } catch (AuthenticationException exception) {
            throw new SignInException(exception.getMessage());
        }
    }

    public TokenRefreshResponse refreshToken(TokenRefreshRequest refreshRequest) {
        String requestRefreshToken = refreshRequest.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    public void confirm(String token) {
        User user = userRepository.findByConfirmationToken(token).orElseThrow(
                () -> new BadRequestException("Invalid token.")
        );

        user.setEnabled(true);
        user.setConfirmationToken("");

        userRepository.save(user);
    }
}
