package com.example.blogproject.service;

import com.example.blogproject.domain.Role;
import com.example.blogproject.domain.User;
import com.example.blogproject.dto.LoginResult;
import com.example.blogproject.repository.RoleRepository;
import com.example.blogproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    // 이름 중복 확인
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    // 이메일 중복 확인
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    //회원가입
    @Transactional
    public boolean registerUser(User user) {
        try {
            // 이름 또는 이메일이 존재하는지 확인
            if (isUsernameTaken(user.getUsername()) || isEmailTaken(user.getEmail())) {
                log.info("Username or email already exists");
                return false;
            }
            // 사용자의 등록 날짜를 현재 시각으로 설정
            user.setRegistrationDate(LocalDateTime.now());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            Optional<Role> defaultRole = roleRepository.findByName("ROLE_USER");
            if (defaultRole.isPresent()) {
                // 기본 역할이 존재하면 역할 설정
                Set<Role> roles = new HashSet<>();
                roles.add(defaultRole.get());
                user.setRoles(roles);
            } else {
                log.error("ROLE_USER not found in the database");
                return false;
            }

            // 사용자 정보 저장
            userRepository.save(user);
            log.info("User registered successfully: {}", user.getUsername());
            return true;
        } catch (Exception e) {
            log.error("Error during user registration", e);
            throw new RuntimeException("User registration failed", e);
        }

    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }




    // 로그인 확인
//    public LoginResult login(String username, String password) {
//        log.info("Attempting login for user: {}", username);
//        Optional<User> userOptional = userRepository.findByUsername(username);
//
//        if (!userOptional.isPresent()) {
//            log.info("Attempting login for user: {}", username);
//            return new LoginResult(false, "ID가 존재하지 않습니다.");
//        }
//
//        User user = userOptional.get();
//        if (!user.getPassword().equals(password)) {
//            log.warn("Login failed: Incorrect password for user - {}", username);
//            return new LoginResult(false, "비밀번호가 일치하지 않습니다.");
//        }
//
//        log.warn("Login failed: Incorrect password for user - {}", username);
//        return new LoginResult(true, "로그인 성공", user);
//    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }


    // 사용자 정의 예외 클래스
    class RoleNotFoundException extends RuntimeException {
        public RoleNotFoundException(String message) {
            super(message);
        }
    }
}
