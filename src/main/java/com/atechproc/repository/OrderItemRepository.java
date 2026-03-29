package com.atechproc.repository;

import com.atechproc.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem , Long> {
    OrderItem findByMedicine_id(Long id);

    @Query("select o from OrderItem o where o.order.pharmacy.id=:pharmacyId and o.order.date=:now order by o.createdAt desc limit 5")
    List<OrderItem> searchLastOrderItems(@Param("pharmacyId") Long id, LocalDate now);
}
