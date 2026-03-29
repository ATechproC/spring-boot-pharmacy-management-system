package com.atechproc.controller;

import com.atechproc.dto.DashboardDetails;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.dashboard.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardService dashboardService;

    @GetMapping("/details")
    public ResponseEntity<ApiResponse> getDashboardDetailsHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        DashboardDetails details = dashboardService.getDashboardDetails(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", details));
    }
}
