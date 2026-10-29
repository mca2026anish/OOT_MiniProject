package com.cleangoa.cleangoa;

import com.cleangoa.cleangoa.entity.Report;
import com.cleangoa.cleangoa.entity.User;
import com.cleangoa.cleangoa.repository.UserRepository;
import com.cleangoa.cleangoa.service.ReportService;

import com.cleangoa.cleangoa.service.ImageStorageService;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;
    private final ImageStorageService imageStorageService;

public ReportController(
        ReportService reportService,
        UserRepository userRepository,
        ImageStorageService imageStorageService) {

    this.reportService = reportService;
    this.userRepository = userRepository;
    this.imageStorageService = imageStorageService;
}

    @GetMapping("/report")
    public String showReportForm() {
        return "report-form";
    }

@PostMapping("/report")
public String submitReport(
        @RequestParam String title,
        @RequestParam String description,
        @RequestParam String locationName,
        @RequestParam(required = false) MultipartFile image,
        @RequestParam(required = false) Double latitude,
        @RequestParam(required = false) Double longitude,
        Authentication authentication,
        Model model,
        RedirectAttributes redirectAttributes) {

        title = title.trim();
        description = description.trim();
        locationName = locationName.trim();

        if ((latitude == null) != (longitude == null)) {
model.addAttribute(
"error",
"Please select a location on the map."
);
return "report-form";
}

if (latitude != null &&
(latitude < -90 || latitude > 90)) {

model.addAttribute(
        "error",
        "Invalid latitude. Please select a valid location."
);
return "report-form";

}

if (longitude != null &&
(longitude < -180 || longitude > 180)) {

model.addAttribute(
        "error",
        "Invalid longitude. Please select a valid location."
);
return "report-form";

}

if (latitude != null && longitude != null) {

if (latitude < 14.8 || latitude > 15.8
        || longitude < 73.6 || longitude > 74.3) {

    model.addAttribute(
            "error",
            "Please select a location within Goa."
    );

    return "report-form";
}

}

        if (title.isBlank()
                || description.isBlank()
                || locationName.isBlank()) {

            model.addAttribute(
                    "error",
                    "Please fill in all the required fields."
            );

            return "report-form";
        }

        if (title.length() > 150
                || description.length() > 2000
                || locationName.length() > 250) {

            model.addAttribute(
                    "error",
                    "One or more fields exceed the allowed length."
            );

            return "report-form";
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("Logged-in user not found")
                );

Report report = new Report();

report.setTitle(title);
report.setDescription(description);
report.setLocationName(locationName);
report.setLatitude(latitude);
report.setLongitude(longitude);

try {
    String imagePath = imageStorageService.storeImage(image);
    report.setImagePath(imagePath);

} catch (IllegalArgumentException e) {

    model.addAttribute("error", e.getMessage());
    return "report-form";

} catch (java.io.IOException e) {

    model.addAttribute(
            "error",
            "Unable to save the image. Please try again.");

    return "report-form";
}

reportService.submitReport(report, user);

        redirectAttributes.addFlashAttribute(
                "success",
                "Your report has been submitted for review."
        );

        return "redirect:/user/reports";
    }
}
