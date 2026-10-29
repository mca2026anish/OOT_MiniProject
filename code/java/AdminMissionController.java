
package com.cleangoa.cleangoa.controller;

import com.cleangoa.cleangoa.entity.Mission;
import com.cleangoa.cleangoa.service.MissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminMissionController {

    private final MissionService missionService;

    public AdminMissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping("/admin/missions")
    public String showMissions(Model model) {
        model.addAttribute("missions", missionService.getAllMissions());
        model.addAttribute("mission", new Mission());
        return "admin-missions";
    }

    @PostMapping("/admin/missions")
    public String createMission(@ModelAttribute Mission mission) {
        missionService.createMission(mission);
        return "redirect:/admin/missions";
    }

    
@GetMapping("/admin/missions/edit/{id}")
public String editMission(@PathVariable Long id, Model model) {
    model.addAttribute("mission", missionService.getMissionById(id));
    model.addAttribute("editing", true);
    return "edit-mission";
}

@PostMapping("/admin/missions/edit/{id}")
public String updateMission(
        @PathVariable Long id,
        @ModelAttribute Mission updatedMission,
        RedirectAttributes redirectAttributes) {

    Mission mission = missionService.getMissionById(id);

    mission.setTitle(updatedMission.getTitle());
    mission.setDescription(updatedMission.getDescription());
    mission.setLocationName(updatedMission.getLocationName());
    mission.setEventDate(updatedMission.getEventDate());
    mission.setMaxVolunteers(updatedMission.getMaxVolunteers());

    missionService.createMission(mission);

    redirectAttributes.addFlashAttribute(
            "message", "Mission updated successfully.");

    return "redirect:/admin/missions";
}

@PostMapping("/admin/missions/delete/{id}")
public String deleteMission(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {

    missionService.deleteMission(id);

    redirectAttributes.addFlashAttribute(
            "message", "Mission deleted successfully.");

    return "redirect:/admin/missions";
}


}
