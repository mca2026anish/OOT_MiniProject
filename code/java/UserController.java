package com.cleangoa.cleangoa;

import com.cleangoa.cleangoa.entity.User;
import com.cleangoa.cleangoa.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

                System.out.println("SIGNUP FORM SUBMITTED");

        name = name.trim();
        email = email.trim().toLowerCase();

        if (name.isBlank() || email.isBlank()) {
            model.addAttribute("error",
                    "Name and email are required.");
            return "signup";
        }

        if (password.length() < 8 || password.length() > 72) {
            model.addAttribute("error",
                    "Password must contain 8 to 72 characters.");
            return "signup";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error",
                    "Passwords do not match.");
            return "signup";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        try {
            userService.registerUser(user);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Account created successfully! You can now log in."
            );

            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }
}
