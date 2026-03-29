package com.atechproc.repository;

import com.atechproc.enums.CREDIT_STATUS;
import com.atechproc.model.Credit;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    Credit findByClientIdAndPharmacy_id(String clientId, Long id);

    List<Credit> findByPharmacy_id(Long id);

    @Query("select c from Credit c where lower(c.clientId) like concat('%', lower(:clientId),'%') and c.pharmacy.id=:id")
    List<Credit> searchFroCreditsByCIN(String clientId, Long id);

    @Query("select c from Credit c where lower(c.name) like concat('%', lower(:name),'%') and c.pharmacy.id=:id")
    List<Credit> searchFroCreditsByName(String name, Long id);

    Long countByPharmacy_id(Long id);

    List<Credit> findByStatusAndPharmacy_id(CREDIT_STATUS status, Long id);
}
