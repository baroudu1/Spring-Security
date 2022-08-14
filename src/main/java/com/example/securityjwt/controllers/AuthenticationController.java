package com.example.securityjwt.controllers;


import com.example.securityjwt.helpers.JWTHelper;
import com.example.securityjwt.models.ERole;
import com.example.securityjwt.models.Role;
import com.example.securityjwt.models.User;
import com.example.securityjwt.payloads.ResponseAuthWithOutJWT;
import com.example.securityjwt.payloads.ResponseAuthentication;
import com.example.securityjwt.payloads.SignInRequest;
import com.example.securityjwt.payloads.SignUpRequest;
import com.example.securityjwt.services.RoleService;
import com.example.securityjwt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController @RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTHelper jwtHelper;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsername(),
                        signInRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtHelper.generateJWT(authentication);

//        System.err.println("accessToken: " + accessToken);

        return ResponseEntity.ok(new ResponseAuthentication("Login Successful", accessToken));


    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new ResponseAuthWithOutJWT("Username is already taken!"));
        }
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ResponseAuthWithOutJWT("Email is already in use!"));
        }
// Create new user's account
        Set<String> requestRoles = signUpRequest.getRoles();

        if (requestRoles == null) {
            return ResponseEntity.badRequest().body(new ResponseAuthWithOutJWT("You should have at least one role!"));
        }

        User user = new User(signUpRequest.getFirstName(),signUpRequest.getLastName(),signUpRequest.getUsername(), signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();


        for (String role : requestRoles) {
            Role dbRole = null;
            try{
                dbRole = roleService.findRoleByName(ERole.valueOf(role));
            }catch (Exception e){
                return ResponseEntity.badRequest().body(new ResponseAuthWithOutJWT("Role " + role + " is not valid!"));
            }

//            System.err.println("dbRole: " + dbRole);
            roles.add(dbRole);
        }
        user.setRoles(roles);

//        System.err.println("user: " + user);
        userService.saveUser(user);

        return ResponseEntity.ok().body(new ResponseAuthWithOutJWT("User registered successfully"));
    }


}

