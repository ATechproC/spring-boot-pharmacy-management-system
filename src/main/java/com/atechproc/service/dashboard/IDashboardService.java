package com.atechproc.service.dashboard;

import com.atechproc.dto.DashboardDetails;

public interface IDashboardService {
    DashboardDetails getDashboardDetails(String jwt) throws Exception;
}
