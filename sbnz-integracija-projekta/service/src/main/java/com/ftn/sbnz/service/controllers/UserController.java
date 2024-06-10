package com.ftn.sbnz.service.controllers;

import com.ftn.sbnz.model.models.FireCompany;
import com.ftn.sbnz.model.models.MessageResponse;
import com.ftn.sbnz.model.models.users.User;
import com.ftn.sbnz.service.dto.*;
import com.ftn.sbnz.service.security.jwtUtils.TokenUtils;
import com.ftn.sbnz.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(value="/register-firefighter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('CAPTAIN', 'CHIEF')")
    public ResponseEntity<?> registerFirefighter(@RequestBody UserDTO dto) {
        UserDataDTO ret = userService.insert(dto, User.UserRole.FIREFIGHTER);
        return new ResponseEntity<UserDataDTO>(ret, HttpStatus.OK);
    }

    @PostMapping(value="/register-captain", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CHIEF')")
    public ResponseEntity<?> registerCaptain(@RequestBody UserDTO dto) {
        UserDataDTO ret = userService.insert(dto, User.UserRole.CAPTAIN);
        return new ResponseEntity<UserDataDTO>(ret, HttpStatus.OK);
    }

    @PostMapping(value="/register-company", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CHIEF')")
    public ResponseEntity<?> insertFireCompany(@RequestBody FireCompanyDTO dto) {
        FireCompanyResponseDTO ret = userService.insertFireCompany(dto);
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @GetMapping(value = "/captain-without-fire-company", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CHIEF')")
    public ResponseEntity<List<UserDataDTO>> getCaptainsWithoutFireCompany() {
        List<UserDataDTO> captainsWithoutFireCompany = userService.getCaptainsWithoutFireCompany();
        return ResponseEntity.ok(captainsWithoutFireCompany);
    }

    @GetMapping(value = "/firefighter-without-fire-company", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CHIEF')")
    public ResponseEntity<List<UserDataDTO>> getFirefightersWithoutFireCompany() {
        List<UserDataDTO> firefightersWithoutFireCompany = userService.getFirefightersWithoutFireCompany();
        return ResponseEntity.ok(firefightersWithoutFireCompany);
    }


}
