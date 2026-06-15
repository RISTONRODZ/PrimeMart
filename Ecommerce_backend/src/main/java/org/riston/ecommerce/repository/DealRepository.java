package org.riston.ecommerce.repository;

import org.riston.ecommerce.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal,Long> {
    void deleteByHomeCategoryId(Long homeCategoryId);
}
