package com.example.blogproject.controller;

import com.example.blogproject.domain.User;
import com.example.blogproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//회원 정보를 위함
@Controller
@RequestMapping("/minlog")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String main(Model model) {
        return "main";
    }

    // 로그인 폼
    @GetMapping("/loginform")
    public String loginForm(Model model) {
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
                               @RequestParam("name") String name,
                               @RequestParam("email") String email,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setName(name);
            user.setEmail(email);

            userService.registerUser(user);

            return "welcome";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/minlog/userreg";
        }
    }

    // 회원가입 환영 메시지
    @GetMapping("/userreg")
    public String welcome(Model model) {
        return "welcome";
    }

    // 로그인

}
