package ua.com.faceit.todolist.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.faceit.todolist.data.payload.request.LoginRequest;
import ua.com.faceit.todolist.data.payload.request.SignupRequest;
import ua.com.faceit.todolist.data.payload.request.TokenRefreshRequest;
import ua.com.faceit.todolist.data.payload.response.JwtResponse;
import ua.com.faceit.todolist.data.payload.response.MessageResponse;
import ua.com.faceit.todolist.data.payload.response.TokenRefreshResponse;
import ua.com.faceit.todolist.service.AuthService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        TokenRefreshResponse tokenRefreshResponse = authService.refreshToken(tokenRefreshRequest);

        return ResponseEntity.ok(tokenRefreshResponse);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        authService.confirm(token);

        return ResponseEntity.ok(new MessageResponse("User confirmation successfully!"));
    }
}
