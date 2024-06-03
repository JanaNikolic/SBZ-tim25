package com.ftn.sbnz.service.services;

import com.ftn.sbnz.model.models.exceptions.UserAlreadyExistsException;
import com.ftn.sbnz.model.models.users.User;
import com.ftn.sbnz.service.dto.UserDTO;
import com.ftn.sbnz.service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> ret = userRepository.findByEmail(email);
        if (!ret.isEmpty()) {
            return org.springframework.security.core.userdetails.User.withUsername(email).password(ret.get().getPassword()).roles(ret.get().getRole().toString()).build();
        }
        throw new UsernameNotFoundException("User not found with this email: " + email);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(""));
    }

    public UserDTO insert(UserDTO dto) {
        try {
            getByEmail(dto.getEmail());
            throw new UserAlreadyExistsException("User with given email already existis.");
        }catch (UsernameNotFoundException ex) {
            User user = new User(null, dto.getEmail(), dto.getName(), dto.getLastname(),
                    passwordEncoder.encode(dto.getPassword()), User.UserRole.USER, true, false);
            userRepository.save(user);
            userRepository.flush();

            return dto;
        }
    }

}
