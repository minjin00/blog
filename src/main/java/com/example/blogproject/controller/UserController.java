package com.example.blogproject.controller;

import com.example.blogproject.domain.User;
import com.example.blogproject.dto.LoginResult;
import com.example.blogproject.repository.UserRepository;
import com.example.blogproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

//회원 정보를 위함
@Controller
@Slf4j
//@RequestMapping("/minlog")
public class UserController {
    @Autowired
    private UserService userService;


    // 로그인 폼
    @GetMapping("/loginform")
    public String loginForm(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/blog";
        }
        return "loginform";
    }


    // 회원가입 폼
    @GetMapping("/userregform")
    public String userregform() {
        return "userregform";
    }


    // 회원가입 요청 처리
    @PostMapping("/userreg")
    public String registerUser(@ModelAttribute User user, @RequestParam("checkPassword") String checkPassword,
                               RedirectAttributes redirectAttributes) {
        if(!user.getPassword().equals(checkPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/userregform";
        }
        try {
            boolean isRegistered = userService.registerUser(user);
            if (isRegistered) {
                return "redirect:/loginform?registered=true";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "이미 존재하는 사용자명 또는 이메일입니다.");
                return "redirect:/userregform";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원가입에 실패했습니다." + e.getMessage());
            return "redirect:/userregform";
        }
    }

    // 회원가입 환영 메시지
    @GetMapping("/userreg")
    public String welcome() {
        return "loginform";
    }

    // 로그인
//    @PostMapping("/login")
//    public String login(@RequestParam("username") String username,
//                        @RequestParam("password") String password,
//                        HttpSession session,
//                        RedirectAttributes redirectAttributes) {
//        log.info("Login attempt for user: {}", username);
//
//        LoginResult result = userService.login(username, password);
//        if (result.isSuccess()) {
//            session.setAttribute("user", result.getUser());
//            return "redirect:/blog";
//        } else {
//            log.warn("Login failed: {}", result.getMessage());
//            redirectAttributes.addFlashAttribute("error", result.getMessage());
//            return "redirect:/loginform";
//        }
//    }

    @GetMapping("/{username}")
    public String userPage(@PathVariable String username, Model model, Principal principal) {
        if (principal == null || !principal.getName().equals(username)) {
            return "redirect:/loginform";
        }
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "blog";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginform";
    }

}
