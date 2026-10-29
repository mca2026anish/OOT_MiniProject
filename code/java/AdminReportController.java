package com.cleangoa.cleangoa;

import com.cleangoa.cleangoa.entity.Report;
import com.cleangoa.cleangoa.service.ReportService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminReportController {

private final ReportService reportService;

public AdminReportController(ReportService reportService) {
    this.reportService = reportService;
}

@GetMapping("/admin/reports")
public String viewReports(
        @org.springframework.web.bind.annotation.RequestParam(
                required = false) String status,
        Model model) {

    List<Report> reports;

    if (status == null || status.equals("ALL")) {
        reports = reportService.getAllReports();
    } else if (status.equals("PENDING")
            || status.equals("APPROVED")
            || status.equals("REJECTED")) {
        reports = reportService.getReportsByStatus(status);
    } else {
        status = "ALL";
        reports = reportService.getAllReports();
    }

    model.addAttribute("reports", reports);
    model.addAttribute("selectedStatus", status);

    return "admin-reports";
}

@PostMapping("/admin/reports/{id}/approve")
public String approveReport(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {

    reportService.updateReportStatus(id, "APPROVED");

    redirectAttributes.addFlashAttribute(
            "success", "Report approved successfully.");

    return "redirect:/admin/reports";
}

@PostMapping("/admin/reports/{id}/reject")
public String rejectReport(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {

    reportService.updateReportStatus(id, "REJECTED");

    redirectAttributes.addFlashAttribute(
            "success", "Report rejected successfully.");

    return "redirect:/admin/reports";
}


@PostMapping("/admin/reports/{id}/delete")
public String deleteReport(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {

    reportService.deleteReport(id);

    redirectAttributes.addFlashAttribute(
            "success", "Report deleted successfully.");

    return "redirect:/admin/reports";
}

}