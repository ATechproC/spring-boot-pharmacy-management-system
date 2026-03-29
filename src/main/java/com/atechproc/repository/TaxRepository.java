package com.atechproc.repository;

import com.atechproc.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxRepository extends JpaRepository<Tax, Long> {
    Tax findByNameAndYearMonthAndPharmacy_id(String name, String string, Long id);

    List<Tax> findByPharmacy_id(Long id);

    Tax findByIdAndIsActive(Long id, boolean b);
}
