package com.atechproc.service.report;

import com.atechproc.dto.MedicineDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.mapper.OrderMapper;
import com.atechproc.model.Order;
import com.atechproc.model.Pharmacy;
import com.atechproc.model.Report;
import com.atechproc.model.User;
import com.atechproc.service.Order.IOrderService;
import com.atechproc.service.medicine.IMedicineService;
import com.atechproc.service.pharmacy.IPharmacyService;
import com.atechproc.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {

    private final IOrderService orderService;
    private final IMedicineService medicineService;
    private final IUserService userService;
    private final IPharmacyService pharmacyService;

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public Report getTodayReport(String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }
        List<Order> orders = orderService.getTodayOrders(jwt);
        List<MedicineDto> topSellingMedicines = medicineService.getTodayTopSellingMedicines(jwt);
        return getReport(orders, topSellingMedicines, jwt);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public Report getThisMonthReport(String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }
        List<Order> orders = orderService.getThisMonthOrders(jwt);
        List<MedicineDto> topSellingMedicines = medicineService.getCurrentMonthTopSellingMedicines(jwt);
        return getReport(orders, topSellingMedicines, jwt);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public Report getThisYearReport(String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }
        List<Order> orders = orderService.getThisYearOrders(jwt);
        List<MedicineDto> topSellingMedicines = medicineService.getCurrentYearTopSellingMedicines(jwt);
        return getReport(orders, topSellingMedicines, jwt);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public Report getThisWeekReport(String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }
        List<Order> orders = orderService.getThisWeekOrders(jwt);
        List<MedicineDto> topSellingMedicines = medicineService.getCurrentWeekTopSellingMedicines(jwt);
        return getReport(orders, topSellingMedicines, jwt);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public Report getDayReport(String jwt, LocalDate date) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }
        List<Order> orders = orderService.getDayOrders(jwt, date);
        List<MedicineDto> medicines = medicineService.getDayTopSellingMedicines(jwt, date);
        return getReport(orders, medicines, jwt);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public Report getMonthReport(String jwt, YearMonth yearMonth) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }
        List<Order> orders = orderService.getMonthOrders(jwt, yearMonth);
        List<MedicineDto> medicines = medicineService.getMonthTopSellingMedicines(jwt, yearMonth);
        return getReport(orders, medicines, jwt);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public Report getYearReport(String jwt, int year) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }
        List<Order> orders = orderService.getYearOrders(jwt, year);
        List<MedicineDto> medicines = medicineService.getYearTopSellingMedicines(jwt, year);
        return getReport(orders, medicines, jwt);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public Report getWeekOfMonthReport(String jwt, int weekOfMonth, YearMonth yearMonth) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }
        List<Order> orders = orderService.getWeekOfMonthOrders(jwt, weekOfMonth, yearMonth);
        List<MedicineDto> medicines = medicineService.getWeekTopSellingMedicines(jwt, weekOfMonth, yearMonth);
        return getReport(orders, medicines, jwt);
    }

    private Report getReport(List<Order> orders, List<MedicineDto> topSellingMedicines, String jwt) {
        BigDecimal totalEarnings = BigDecimal.ZERO;
        BigDecimal totalProfit = BigDecimal.ZERO;

        for (Order order : orders) {
            totalEarnings = totalEarnings.add(order.getTotal());
            totalProfit = totalProfit.add(order.getProfit());
        }

        Report report = new Report();
        report.setTotalEarnings(totalEarnings);
        report.setTotalProfit(totalProfit);
        report.setTotalTransactions((long) orders.size());
        report.setOrders(OrderMapper.toOrderDTOs(orders));
        report.setTopSellingMedicines(topSellingMedicines);

        return report;
    }
}
