package com.atechproc.repository;

import com.atechproc.enums.MEDICINE_STATUS;
import com.atechproc.enums.MEDICINE_TYPE;
import com.atechproc.model.Medicine;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    @Modifying
    @Transactional
    @Query("update Medicine m set m.status = 'EXPIRED' where m.expiredDate < :now")
    void markExpiredMedicines(@Param("now") YearMonth now);

    List<Medicine> findByStatusAndPharmacy_id(MEDICINE_STATUS medicineStatus, Long id);

    @Query("select m from Medicine m where lower(m.batchNumber) like lower(concat('%', :keyword, '%'))" +
            " and m.pharmacy.id=:id and m.status='AVAILABLE' and m.active=true")
    List<Medicine> searchForMedicineByBatchNumber(@Param("keyword") String keyword, @Param("id") Long id);

    @Query("select m from Medicine m where m.category.id=:groupId and lower(m.name) like concat('%', :keyword, '%') and pharmacy.id=:pharmacyId and m.active=true")
    List<Medicine> searchForMedicineByGroupAndName(@Param("pharmacyId") Long id,@Param("groupId") Long groupId, String keyword);

    @Query("select m from Medicine m where m.supplier.id=:supplierId and lower(m.name) like concat('%', :keyword, '%') and pharmacy.id=:pharmacyId and m.active=true")
    List<Medicine> searchForMedicineBySupplierAndName(@Param("pharmacyId") Long id,@Param("supplierId") Long supplierId, String keyword);

    Medicine findByIdAndActive(Long medicineId, boolean b);

    List<Medicine> findByStatusAndPharmacy_idAndActive(MEDICINE_STATUS status, Long id, boolean b);

    Medicine findByBatchNumberAndNameAndActiveAndPharmacy_id(String batchNumber, String medicineName, boolean b, Long id);

    List<Medicine> findByTypeAndPharmacy_idAndActive(MEDICINE_TYPE type, Long id, boolean b);

    List<Medicine> findByStatusAndInStockAndPharmacy_idAndActive(MEDICINE_STATUS medicineStatus, boolean b, Long id, boolean b1);

    @Query("select m from Medicine m where lower(m.name) like concat('%', lower(:keyword), '%') and m.pharmacy.id=:pharmacyId and m.active=true and m.status <> 'EXPIRED' ")
    List<Medicine> searchForMedicineAndActive(@Param("keyword") String keyword,@Param("pharmacyId") Long id);

    List<Medicine> findByPharmacy_idAndActive(Long id, boolean b);

    Long countByStatusAndPharmacy_idAndActive(MEDICINE_STATUS medicineStatus, Long id, boolean b);

    Long countByInStockAndPharmacy_idAndActive(boolean b, Long id, boolean b1);

    Long countByPharmacy_idAndActive(Long id, boolean b);

    Medicine findByNameAndBatchNumberAndPharmacy_id(@NotNull(message = "Medicine name is required") String name, String batchNumber, Long id);

    List<Medicine> findByPharmacy_idAndSupplier_idAndActive(Long id, Long id1, boolean b);

    Long countByPharmacy_idAndSupplier_idAndActive(Long pharmacyId, Long supplierId, boolean b);

    Long countByPharmacy_idAndCategory_idAndActive(Long pharmacyId, Long groupId, boolean b);

    @Query("select m from Medicine m where m.pharmacy.id=:pharmacyId and lower(m.name) like concat('%', lower(:keyword), '%') and m.status='EXPIRED'")
    List<Medicine> searchFormExpiredMedicinesByName(@Param("keyword") String keyword,@Param("pharmacyId") Long id);

    Long countByInStockAndPharmacy_idAndStatusAndInStockAndActive(boolean b, Long id, MEDICINE_STATUS medicineStatus, boolean b1, boolean b2);

    @Query("select m from Medicine m inner join OrderItem o on m.id=o.medicine.id where m.pharmacy.id=:pharmacyId and o.order.date=:now order by o.quantity desc limit 6")
    List<Medicine> getCurrentDayTopSellingMedicines(@Param("pharmacyId") Long id,@Param("now") LocalDate now);

    @Query("select m from Medicine m inner join OrderItem o on m.id=o.medicine.id where m.pharmacy.id=:pharmacyId and o.order.weekOfMonth=:currentWeekOfMonth and o.order.yearMonth=:yearMonth order by o.quantity desc limit 6")
    List<Medicine> getCurrentWeekTopSellingMedicines(@Param("pharmacyId") Long id,@Param("currentWeekOfMonth") int currentWeekOfMonth,@Param("yearMonth") String yearMonth);

    @Query("select m from Medicine m inner join OrderItem o on m.id=o.medicine.id where m.pharmacy.id=:pharmacyId and o.order.yearMonth=:yearMonth order by o.quantity desc limit 6")
    List<Medicine> getCurrentMonthTopSellingMedicines(@Param("pharmacyId") Long id,@Param("yearMonth") String yearMonth);

    @Query("select m from Medicine m inner join OrderItem o on m.id=o.medicine.id where m.pharmacy.id=:pharmacyId and o.order.year=:year order by o.quantity desc limit 6")
    List<Medicine> getCurrentYearTopSellingMedicines(@Param("pharmacyId") Long id,@Param("year") int year);

    @Query("select m from Medicine m inner join OrderItem o on m.id=o.medicine.id where m.pharmacy.id=:pharmacyId and o.order.date=:date order by o.quantity desc limit 6")
    List<Medicine> getDayTopSellingMedicines(@Param("pharmacyId") Long id, LocalDate date);

    @Query("select m from Medicine m inner join OrderItem o on m.id=o.medicine.id where m.pharmacy.id=:pharmacyId and o.order.yearMonth=:yearMonth order by o.quantity desc limit 6")
    List<Medicine> getMonthTopSellingMedicines(@Param("pharmacyId") Long id, String yearMonth);

    @Query("select m from Medicine m inner join OrderItem o on m.id=o.medicine.id where m.pharmacy.id=:pharmacyId and o.order.year=:year order by o.quantity desc limit 6")
    List<Medicine> getYearTopSellingMedicines(@Param("pharmacyId") Long id, int year);

    @Query("select m from Medicine m inner join OrderItem o on m.id=o.medicine.id where m.pharmacy.id=:pharmacyId and o.order.weekOfMonth=:weekOfMonth and o.order.yearMonth=:yearMonth order by o.quantity desc limit 6")
    List<Medicine> getWeekTopSellingMedicines(@Param("pharmacyId") Long id, int weekOfMonth,@Param("yearMonth") String yearMonth);

    @Query("select m from Medicine m where m.pharmacy.id=:id and m.active=true and lower(m.name) like concat('%', lower(:name), '%') and lower(m.batchNumber) like concat('%', lower(:batchNumber), '%')")
    List<Medicine> searchByNameAndBatchNumber(Long id, String name, String batchNumber);

    List<Medicine> findByPharmacy_idAndInStockAndActive(Long id, boolean b, boolean b1);

    List<Medicine> findByPharmacy_idAndLowAndActive(Long id, boolean b, boolean b1);
}
