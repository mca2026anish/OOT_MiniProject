package com.cleangoa.cleangoa;

import com.cleangoa.cleangoa.entity.Report;
import com.cleangoa.cleangoa.entity.User;
import com.cleangoa.cleangoa.repository.UserRepository;
import com.cleangoa.cleangoa.service.ReportService;
import com.cleangoa.cleangoa.service.MissionService;

import com.cleangoa.cleangoa.entity.Mission;
import com.cleangoa.cleangoa.service.MissionRegistrationService;

import com.cleangoa.cleangoa.entity.MissionRegistration;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

private final UserRepository userRepository;
private final ReportService reportService;

private final MissionService missionService;

private final MissionRegistrationService registrationService;

public DashboardController(
        UserRepository userRepository,
        ReportService reportService,
        MissionService missionService,
        MissionRegistrationService registrationService) {

    this.userRepository = userRepository;
    this.reportService = reportService;
    this.missionService = missionService;
    this.registrationService = registrationService;
}

@GetMapping("/login")
public String loginPage() {
    return "login";
}

@GetMapping("/dashboard")
public String dashboard(Authentication authentication) {

    boolean isAdmin = authentication.getAuthorities()
            .stream()
            .anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_ADMIN"));

    if (isAdmin) {
        return "redirect:/admin/dashboard";
    }

    return "redirect:/user/dashboard";
}

@GetMapping("/user/dashboard")
public String userDashboard() {
    return "user-dashboard";
}

@GetMapping("/admin/dashboard")
public String adminDashboard(Model model) {

    model.addAttribute(
            "totalReports", reportService.getTotalReports());

    model.addAttribute(
            "pendingReports", reportService.getPendingReports());

    model.addAttribute(
            "approvedReports", reportService.getApprovedReports());

    model.addAttribute(
            "rejectedReports", reportService.getRejectedReports());

    return "admin-dashboard";
}

@GetMapping("/user/reports")
public String userReports(
        Authentication authentication,
        Model model) {

    User user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() ->
                    new IllegalStateException("User not found"));

    List<Report> reports = reportService.getUserReports(user);

    model.addAttribute("reports", reports);

    return "user-reports";
}



@GetMapping("/user/missions")
public String showUserMissions(
        Authentication authentication,
        Model model) {

    User user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() ->
                    new IllegalStateException("User not found"));

    List<Mission> missions = missionService.getAllMissions();

    model.addAttribute("missions", missions);

    java.util.Map<Long, Long> volunteerCounts =
            new java.util.HashMap<>();

    java.util.Set<Long> joinedMissionIds =
            new java.util.HashSet<>();

    for (Mission mission : missions) {
        volunteerCounts.put(
                mission.getId(),
                registrationService.getVolunteerCount(mission)
        );
    }

    List<MissionRegistration> registrations =
            registrationService.getUserRegistrations(user);

    for (MissionRegistration registration : registrations) {
        joinedMissionIds.add(registration.getMission().getId());
    }

    model.addAttribute("volunteerCounts", volunteerCounts);
    model.addAttribute("joinedMissionIds", joinedMissionIds);

    return "user-missions";
}



}
