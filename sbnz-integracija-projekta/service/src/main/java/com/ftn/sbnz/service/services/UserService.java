package com.ftn.sbnz.service.services;

import com.ftn.sbnz.model.models.FireCompany;
import com.ftn.sbnz.model.models.exceptions.CustomException;
import com.ftn.sbnz.model.models.exceptions.UserAlreadyExistsException;
import com.ftn.sbnz.model.models.users.User;
import com.ftn.sbnz.service.dto.FireCompanyDTO;
import com.ftn.sbnz.service.dto.FireCompanyResponseDTO;
import com.ftn.sbnz.service.dto.UserDTO;
import com.ftn.sbnz.service.dto.UserDataDTO;
import com.ftn.sbnz.service.repositories.FireCompanyRepository;
import com.ftn.sbnz.service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FireCompanyRepository fireCompanyRepository;
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
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    public UserDataDTO insert(UserDTO dto, User.UserRole role) {
        try {
            getByEmail(dto.getEmail());
            throw new UserAlreadyExistsException("User with given email already exists.");
        } catch (UsernameNotFoundException ex) {
            User user = new User(null, dto.getEmail(), dto.getName(), dto.getLastname(),
                    passwordEncoder.encode(dto.getPassword()), role, true, false);
            user = userRepository.save(user);
            userRepository.flush();

            return new UserDataDTO(user);
        }
    }

    public FireCompanyResponseDTO insertFireCompany(FireCompanyDTO dto) {
        User captain = getByEmail(dto.getCaptain());
        if (captain.getRole() != User.UserRole.CAPTAIN) throw new CustomException("User must be captain!");

        FireCompany existingFireCompany = fireCompanyRepository.findByCaptain(captain).orElse(null);
        if (existingFireCompany != null) {
            throw new CustomException("There is already a Fire Company with this captain!");
        }

        Set<User> firefighters = new HashSet<>();
        for (String f : dto.getFirefighters()) {
            User firefighter = getByEmail(f);
            if (firefighter.getRole() != User.UserRole.FIREFIGHTER) throw new CustomException("User must be firefighter!");

            FireCompany fireCompany = fireCompanyRepository.findByFirefightersContains(firefighter).orElse(null);
            if (fireCompany != null) {
                throw new CustomException("Firefighter " + firefighter.getName() + " " + firefighter.getSurname() + " is already assigned to another fire company!");
            }
            firefighters.add(firefighter);
        }

        FireCompany fireCompany = new FireCompany(null, captain, firefighters);
        fireCompany = fireCompanyRepository.save(fireCompany);
        return new FireCompanyResponseDTO(fireCompany);
    }

    public FireCompany getFireCompanyByCaptain(User captain){
        return fireCompanyRepository.findByCaptain(captain).orElseThrow(() -> new CustomException("Fire Company not found fot this captain!"));
    }


    public FireCompany getFireCompany(User user) {
        if (user.getRole() == User.UserRole.CAPTAIN) return getFireCompanyByCaptain(user);
        return fireCompanyRepository.findByFirefightersContains(user).orElseThrow(() -> new CustomException("Fire Company not found fot this firefighter!"));
    }

    public List<UserDataDTO> getCaptainsWithoutFireCompany() {
        List<User> captains = userRepository.findAllByRole(User.UserRole.CAPTAIN);
        List<UserDataDTO> captainsWithoutCompany = new ArrayList<>();
        for (User captain : captains) {
            FireCompany company = fireCompanyRepository.findByCaptain(captain).orElse(null);
            if (company == null) captainsWithoutCompany.add(new UserDataDTO(captain));
        }
        return captainsWithoutCompany;
    }

    public List<UserDataDTO> getFirefightersWithoutFireCompany() {
        List<User> firefighters = userRepository.findAllByRole(User.UserRole.FIREFIGHTER);
        List<UserDataDTO> firefightersWithoutCompany = new ArrayList<>();
        for (User firefighter : firefighters) {
            FireCompany company = fireCompanyRepository.findByFirefightersContains(firefighter).orElse(null);
            if (company == null) firefightersWithoutCompany.add(new UserDataDTO(firefighter));
        }
        return firefightersWithoutCompany;
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException("User not found!"));
    }
}
