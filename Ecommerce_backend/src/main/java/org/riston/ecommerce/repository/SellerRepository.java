package org.riston.ecommerce.repository;

import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {
    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus status);
}
