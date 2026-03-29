package com.atechproc.repository;

import com.atechproc.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByPharmacy_id(Long id);

    List<Order> findByPharmacy_idAndDayAndYearMonth(Long id, int dayOfMonth, String string);

    List<Order> findByPharmacy_idAndYearMonth(Long id, String string);

    List<Order> findByPharmacy_idAndYear(Long id, int year);

    List<Order> findByPharmacy_idAndWeekOfMonthAndYearMonth(Long id, int currentWeekOfMonth, String string);

    List<Order> findByPharmacy_idAndDate(Long id, LocalDate date);
}
