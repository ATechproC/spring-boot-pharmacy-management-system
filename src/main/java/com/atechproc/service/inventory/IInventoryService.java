package com.atechproc.service.inventory;

import com.atechproc.dto.DashboardDetails;
import com.atechproc.dto.InventoryDetails;

public interface IInventoryService {
    InventoryDetails getInventoryDetails(String jwt);
}
