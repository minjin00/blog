package com.example.blogproject.service;

import com.example.blogproject.domain.Role;
import com.example.blogproject.domain.User;
import com.example.blogproject.repository.RoleRepository;
import com.example.blogproject.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    // 이름 중복 확인
    public boolean isUsernameTaken(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent(); //존재 여부 확인
    }

    // 이메일 중복 확인
    public boolean isEmailTaken(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent(); //존재 여부 확인
    }

    //회원가입
    @Transactional
    public boolean registerUser(User user) {
        try {
//            // 이름 또는 이메일이 존재하는지 확인
//            if (isUsernameTaken(user.getUsername()) || isEmailTaken(user.getEmail())) {
//                log.info("Username or email already exists");
//                return false;
//            }
            // 사용자의 등록 날짜를 현재 시각으로 설정
            user.setRegistrationDate(LocalDateTime.now());

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

    // 로그인
    public void login(String username, String password, HttpServletRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이디입니다."));
        if (!user.getPassword().equals(password))       throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(NoSuchElementException::new);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
