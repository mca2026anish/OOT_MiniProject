package com.cleangoa.cleangoa;

import com.cleangoa.cleangoa.entity.Report;
import com.cleangoa.cleangoa.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MapController {

    private final ReportService reportService;

    public MapController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/map")
    public String showMap(Model model) {

        List<Report> reports = reportService.getAllReports();

        List<Map<String, Object>> mapReports = new ArrayList<>();

        for (Report report : reports) {

            if (report.getLatitude() != null
                    && report.getLongitude() != null) {

                Map<String, Object> item = new LinkedHashMap<>();

                item.put("title", report.getTitle());
                item.put("location", report.getLocationName());
                item.put("latitude", report.getLatitude());
                item.put("longitude", report.getLongitude());
                item.put("status", report.getStatus());
                item.put("imagePath", report.getImagePath());
                item.put("createdAt", report.getCreatedAt().toString());
                item.put("description", report.getDescription());

                mapReports.add(item);
            }
        }

        model.addAttribute("mapReports", mapReports);

        return "map";
    }
}
