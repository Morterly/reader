package com.reader.service;

import com.reader.dto.UserCreateEditDto;
import com.reader.dto.UserReadDto;
import com.reader.entity.User;
import com.reader.mapper.UserCreateEditMapper;
import com.reader.mapper.UserReadMapper;
import com.reader.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCreateEditMapper userCreateEditMapper;
    private final UserReadMapper userReadMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    public UserCreateEditDto findById(Long id) {
        return userRepository.findById(id)
                .map(userCreateEditMapper::mapFrom)
                .orElseThrow();
    }

    public UserReadDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    public UserReadDto save(UserCreateEditDto userDto) {
        User user = userCreateEditMapper.map(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return Optional.of(userRepository.saveAndFlush(user))
                .map(userReadMapper::map)
                .orElseThrow();
    }
}
