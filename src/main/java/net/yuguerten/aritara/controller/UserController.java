package net.yuguerten.aritara.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.yuguerten.aritara.dto.LoginDTO;
import net.yuguerten.aritara.dto.UserDTO;
import net.yuguerten.aritara.model.User;
import net.yuguerten.aritara.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/sign-up")
    public String showSignupForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String registerUser(@ModelAttribute UserDTO userDTO, Model model) {
        try {
            User user = userService.registerNewUser(userDTO);
            model.addAttribute("user", user);
            return "redirect:/user/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "sign-up";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute LoginDTO loginDTO, HttpSession session, Model model) {
        User user = userService.validateUser(loginDTO);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("user", loggedInUser);
            return "profile";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user, HttpSession session, Model model, @RequestParam String password, @RequestParam String confirmPassword) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("user", loggedInUser);
            return "profile";
        }

        // Update user details
        loggedInUser.setUsername(user.getUsername());
        loggedInUser.setEmail(user.getEmail());
        if (!password.isEmpty()) {
            loggedInUser.setPassword(password);
        }

        userService.updateUser(loggedInUser);
        session.setAttribute("loggedInUser", loggedInUser); // Update session with new user details

        model.addAttribute("success", "Profile updated successfully");
        model.addAttribute("user", loggedInUser);
        return "profile";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
        HttpSession session = request.getSession(false);
        model.addAttribute("loginDTO", new LoginDTO());
        if (session != null) {
            session.invalidate();
        }
        return "login";
    }
}
