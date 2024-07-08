package com.example.blogproject.controller;

import com.example.blogproject.domain.User;
import com.example.blogproject.dto.LoginResult;
import com.example.blogproject.repository.UserRepository;
import com.example.blogproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//회원 정보를 위함
@Controller
@RequestMapping("/minlog")
public class UserController {
    @Autowired
    private UserService userService;
    private UserRepository userRepository;

    @GetMapping("")
    public String mainPage() {
       return "main";
    }

    // 로그인 폼
    @GetMapping("/loginform")
    public String loginForm(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/minlog/dashboard";  // 이 부분 추가
        }
        return "loginform";
    }


    // 회원가입 폼
    @GetMapping("/userregform")
    public String userregform(Model model) {
        return "userregform";
    }


    // 회원가입 요청 처리
    @PostMapping("/userreg")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("checkPassword") String checkPassword,
                               @RequestParam("name") String name,
                               @RequestParam("email") String email,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if(!password.equals(checkPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/minlog/userregform";
        }
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setName(name);
            user.setEmail(email);
            boolean isRegistered = userService.registerUser(user);
            if (isRegistered) {
                model.addAttribute("username", username);
                return "welcome";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "이미 존재하는 사용자명 또는 이메일입니다.");
                return "redirect:/minlog/userregform";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원가입에 실패했습니다." + e.getMessage());
            return "redirect:/minlog/userregform";
        }
    }

    // 회원가입 환영 메시지
    @GetMapping("/userreg")
    public String welcome(Model model) {
        return "loginform";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        LoginResult result = userService.login(username, password);
        if (result.isSuccess()) {
            session.setAttribute("user", result.getUser());
            return "redirect:/minlog/blog";
        } else {
            redirectAttributes.addFlashAttribute("error", result.getMessage());
            return "redirect:/minlog/loginform";
        }
    }

    @GetMapping("/{username}")
    public String userPage(@PathVariable String username, Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || !sessionUser.getUsername().equals(username)) {
            return "redirect:/minlog/loginform";
            // 로그인되지 않았거나 다른 사용자의 페이지에 접근 시 로그인 페이지로 리다이렉트
        }

        model.addAttribute("user", sessionUser);
        return "blog";
    }

    @GetMapping("/blog")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/minlog/loginform";
        }
        return "blog";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/minlog/loginform";
    }

}
