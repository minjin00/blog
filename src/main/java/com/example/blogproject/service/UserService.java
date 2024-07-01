package com.example.blogproject.service;

import com.example.blogproject.domain.Role;
import com.example.blogproject.domain.User;
import com.example.blogproject.repository.RoleRepository;
import com.example.blogproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // 이름 중복 확인
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    // 이메일 중복 확인
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public boolean registerUser(User user) {
        // 이름 또는 이메일이 존재하는지 확인
        if (isUsernameTaken(user.getUsername()) || isEmailTaken(user.getEmail())) {
            return false;
        }

        // 사용자의 등록 날짜를 현재 시각으로 설정
        user.setRegistrationDate(LocalDateTime.now());

        Optional<Role> defaultRole = roleRepository.findByName("ROLE_USER");
        if(defaultRole.isPresent()) {
            // 기본 역할이 존재하면 역할 설정
            Set<Role> roles = new HashSet<>();
            roles.add(defaultRole.get());
            user.setRoles(roles);
        } else {
            throw new RoleNotFoundException("역할을 찾을 수 없습니다.");
        }

        // 사용자 정보 저장
        userRepository.save(user);
        return true;
    }

    // 로그인 확인
    public User login(Long loginId, String password) {
        return userRepository.findById(loginId)
                .filter(user-> user.getPassword().equals(password))
                .orElse(null);
    }
}
// 사용자 정의 예외 클래스
class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
