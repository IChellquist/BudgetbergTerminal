package IChellquist.BudgetbergTerminal.controllers;

import IChellquist.BudgetbergTerminal.models.ERole;
import IChellquist.BudgetbergTerminal.models.Role;
import IChellquist.BudgetbergTerminal.models.User;
import IChellquist.BudgetbergTerminal.repository.RoleRepository;
import IChellquist.BudgetbergTerminal.repository.UserRepository;
import IChellquist.BudgetbergTerminal.security.jwt.JwtUtils;
import IChellquist.BudgetbergTerminal.security.payload.request.LoginRequest;
import IChellquist.BudgetbergTerminal.security.payload.request.SignupRequest;
import IChellquist.BudgetbergTerminal.security.payload.response.JwtResponse;
import IChellquist.BudgetbergTerminal.security.payload.response.MessageResponse;
import IChellquist.BudgetbergTerminal.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwt;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${default_user}")
    String defaulEmail;
    @Value("${default_password}")
    String defaultPassword;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        log.info("Login request received from user: {}", loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        JwtResponse response = JwtResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(roles)
                .token(jwtToken)
                .type("Bearer")
                .build()
                ;
        return ResponseEntity.ok(response);

    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){

        if (userRepository.existsByUsername(signupRequest.getEmail().toLowerCase())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: UserName is taken"));
        }

        userRepository.save(User.builder()
                .username(signupRequest.getEmail())
                .password(encoder.encode(signupRequest.getPassword()))
                .roles(new HashSet<Role>(Arrays.asList(Role.builder().name(ERole.ROLE_USER).build())))
                .build()
        );

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));


    }





}
