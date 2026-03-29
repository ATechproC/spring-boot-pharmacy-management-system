package com.atechproc.repository;

import com.atechproc.model.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where lower(c.name) like lower(concat('%', :keyword, '%')) and c.pharmacy.id=:pharmacyId and c.active=true")
    List<Category> searchForCategory(@Param("keyword") String keyword,@Param("pharmacyId") Long pharmacyId );

    Category findByIdAndActive(Long id, boolean b);

    Category findByNameAndPharmacy_idAndActive(@NotBlank(message = "Category name is required") String name, Long id, boolean b);

    Collection<Object> findByPharmacy_idAndActive(Long id, boolean b);

    Long countByPharmacy_idAndActive(Long id, boolean b);

    Category findByNameAndPharmacy_id(@NotBlank(message = "Category name is required") String name, Long id);
}
