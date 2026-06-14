package org.riston.ecommerce.repository;

import jakarta.transaction.Transactional;
import org.riston.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUserId(Long id);
    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.couponCode = null WHERE c.couponCode = :code")
    void removeCouponFromAllCarts(@Param("code") String code);

}
