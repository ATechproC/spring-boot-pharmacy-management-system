package com.atechproc.controller;


import com.atechproc.dto.DashboardDetails;
import com.atechproc.dto.InventoryDetails;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.inventory.IInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final IInventoryService inventoryService;

    @GetMapping("/details")
    public ResponseEntity<ApiResponse> getInventoryDetailsHandler(
            @RequestHeader("Authorization") String jwt
    ) {
        InventoryDetails details = inventoryService.getInventoryDetails(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", details));
    }

}
