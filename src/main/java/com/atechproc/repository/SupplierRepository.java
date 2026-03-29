package com.atechproc.repository;

import com.atechproc.model.Supplier;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByName(String name);

    Supplier findByNameAndPharmacy_id(String name, Long id);

    @Query("select s from Supplier s where lower(s.name) like lower(concat('%', :keyword, '%')) and s.pharmacy.id=:pharmacyId and s.active=true")
    List<Supplier> searchForSupplier(@Param("keyword") String keyword, @Param("pharmacyId") Long pharmacyId);

    List<Supplier> findByPharmacy_id(Long id);

    Supplier findByIdAndActive(Long id, boolean b);

    Supplier findByNameAndPharmacy_idAndActive(@NotBlank(message = "Supplier name is required") String name, Long id, boolean b);

    List<Supplier> findByPharmacy_idAndActive(Long id, boolean b);

    Long countByPharmacy_idAndActive(Long id, boolean b);
}
