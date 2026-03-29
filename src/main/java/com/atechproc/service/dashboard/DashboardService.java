package com.atechproc.service.dashboard;

import com.atechproc.dto.DashboardDetails;
import com.atechproc.dto.MedicineDto;
import com.atechproc.model.Order;
import com.atechproc.service.Order.IOrderService;
import com.atechproc.service.medicine.IMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final IOrderService orderService;
    private final IMedicineService medicineService;

    @Override
    public DashboardDetails getDashboardDetails(String jwt) throws Exception {
        List<Order> todayOrders = orderService.getTodayOrders(jwt);

        BigDecimal earnings = BigDecimal.ZERO;
        BigDecimal profit = BigDecimal.ZERO;

        for(Order order : todayOrders) {
            earnings = earnings.add(order.getTotal());
            profit = profit.add(order.getProfit());
        }

        List<MedicineDto> medicines = medicineService.getTodayTopSellingMedicines(jwt);

        DashboardDetails details = new DashboardDetails();
        details.setTotalProfit(profit);
        details.setTotalEarnings(earnings);
        details.setTopSellingMedicines(medicines);
        details.setTotalTransactions((long) todayOrders.size());

        return details;
    }
}
