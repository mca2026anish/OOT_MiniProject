package com.cleangoa.cleangoa.controller;

import com.cleangoa.cleangoa.entity.User;
import com.cleangoa.cleangoa.repository.UserRepository;
import com.cleangoa.cleangoa.service.MissionRegistrationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cleangoa.cleangoa.entity.MissionRegistration;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MissionRegistrationController {

    private final MissionRegistrationService registrationService;
    private final UserRepository userRepository;

    public MissionRegistrationController(
            MissionRegistrationService registrationService,
            UserRepository userRepository) {
        this.registrationService = registrationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/user/missions/join")
    public String joinMission(
            @RequestParam Long missionId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("User not found"));

        String message = registrationService.joinMission(missionId, user);

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/user/missions";
    }

        @PostMapping("/user/missions/leave")
public String leaveMission(
        @RequestParam Long missionId,
        Authentication authentication,
        RedirectAttributes redirectAttributes) {

    User user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() ->
                    new IllegalStateException("User not found"));

    registrationService.leaveMission(missionId, user);

    redirectAttributes.addFlashAttribute(
            "message", "You have left the mission successfully.");

    return "redirect:/user/my-missions";
}

    
@GetMapping("/user/my-missions")
public String showMyMissions(
        Authentication authentication,
        Model model) {

    User user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() ->
                    new IllegalStateException("User not found"));

    List<MissionRegistration> registrations =
            registrationService.getUserRegistrations(user);

    model.addAttribute("registrations", registrations);

    return "user-my-missions";
}


}
