package com.bookstore.controller;

import com.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Registration is intentionally removed — this app runs on a small, fixed
 * pool of shared demo accounts (see DemoAccounts) that visitors pick from
 * the login page instead of creating their own. Both accounts can be used
 * by multiple visitors at the same time; kept simple on purpose since this
 * is just a portfolio demo.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("accounts", userService.getDemoAccounts());
        return "login";
    }
}
