package com.atechproc.controller;

import com.atechproc.model.Report;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.report.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("${api.prefix}/report")
@RequiredArgsConstructor
public class ReportController {

    private final IReportService reportService;

    @GetMapping("/current-day")
    public ResponseEntity<ApiResponse> getTodayReportHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Report report = reportService.getTodayReport(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", report));
    }

    @GetMapping("/current-week")
    public ResponseEntity<ApiResponse> getCurrentWeekReport(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Report report = reportService.getThisWeekReport(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", report));
    }

    @GetMapping("/current-month")
    public ResponseEntity<ApiResponse> getCurrentMonthHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Report report = reportService.getThisMonthReport(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", report));
    }

    @GetMapping("/current-year")
    public ResponseEntity<ApiResponse> getCurrentYearHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Report report = reportService.getThisYearReport(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", report));
    }

    @GetMapping("/day")
    public ResponseEntity<ApiResponse> getDayReportHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestParam LocalDate date
            ) throws Exception {
        Report report = reportService.getDayReport(jwt, date);
        return ResponseEntity.ok(new ApiResponse("Success", report));
    }

    @GetMapping("/week")
    public ResponseEntity<ApiResponse> getWeekReportHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestParam int weekOfMonth,
            @RequestParam YearMonth yearMonth
    ) throws Exception {
        Report report = reportService.getWeekOfMonthReport(jwt, weekOfMonth, yearMonth);
        return ResponseEntity.ok(new ApiResponse("Success", report));
    }

    @GetMapping("/month")
    public ResponseEntity<ApiResponse> getMonthReportHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestParam YearMonth yearMonth
            ) throws Exception {
        Report report = reportService.getMonthReport(jwt, yearMonth);
        return ResponseEntity.ok(new ApiResponse("Success", report));
    }

    @GetMapping("/year")
    public ResponseEntity<ApiResponse> getYearReportHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestParam int year
            ) throws Exception {
        Report report = reportService.getYearReport(jwt, year);
        return ResponseEntity.ok(new ApiResponse("Success", report));
    }


}
