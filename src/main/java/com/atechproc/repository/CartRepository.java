package com.atechproc.repository;

import com.atechproc.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByPharmacy_id(Long pharmacyId);
}
