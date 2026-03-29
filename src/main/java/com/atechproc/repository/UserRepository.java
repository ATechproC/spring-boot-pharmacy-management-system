package com.atechproc.repository;

import com.atechproc.enums.USER_ROLE;
import com.atechproc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);

    List<User> findByRoleAndPharmacy_id(USER_ROLE userRole, Long id);

    Long countByRoleAndPharmacy_id(USER_ROLE userRole, Long id);

    List<User> findByRole(USER_ROLE userRole);
}
