package com.atechproc.repository;

import com.atechproc.enums.MEDICINE_STATUS;
import com.atechproc.model.Medicine;
import com.atechproc.model.Pharmacy;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    Pharmacy findByOwner_id(Long id);
}
