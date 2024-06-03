package com.ftn.sbnz.service.controllers;

import com.ftn.sbnz.model.models.users.User;
import com.ftn.sbnz.service.dto.CredentialsDTO;
import com.ftn.sbnz.service.dto.TokenDTO;
import com.ftn.sbnz.service.dto.UserDTO;
import com.ftn.sbnz.service.security.jwtUtils.TokenUtils;
import com.ftn.sbnz.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody CredentialsDTO credentials) {
        Authentication authentication;

        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails user = (UserDetails) authentication.getPrincipal();
        User userFromdb = this.userService.getByEmail(credentials.getEmail());
        if (!userFromdb.isEnabled()) {
            return new ResponseEntity<String>(" This account have not been activated yet!", HttpStatus.UNAUTHORIZED);
        }
        String jwt = tokenUtils.generateToken(user, userFromdb);

        return new ResponseEntity<TokenDTO>(new TokenDTO(jwt, ""), HttpStatus.OK);
    }

    @PostMapping(value="/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody UserDTO dto) {
        UserDTO ret = userService.insert(dto);
        return new ResponseEntity<UserDTO>(ret, HttpStatus.OK);
    }
}
