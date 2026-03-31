package com.atechproc.repository;

import com.atechproc.model.Pharmacy;
import com.atechproc.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxRepository extends JpaRepository<Tax, Long> {
    Tax findByNameAndYearMonthAndPharmacy_id(String name, String string, Long id);
    void deleteByPharmacy_id(Long id);
    List<Tax> findByPharmacy_idAndYearMonth(Long id, String string);
    List<Tax> findByPharmacy_idAndYear(Long id,int year);

    Long countByPharmacy_id(Long id);
}