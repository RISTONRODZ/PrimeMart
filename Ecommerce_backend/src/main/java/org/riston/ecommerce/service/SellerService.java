package org.riston.ecommerce.service;

import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.response.SellerStatusResponse;

import java.util.List;

public interface SellerService {
    Seller getSellerProfile(String jwt);

    Seller createSeller(Seller seller);

    Seller getSellerById(Long id);

    Seller getSellerByEmail(String email);

    List<Seller> getAllSellers(AccountStatus status);

    Seller updateSeller(Long id, Seller seller);

    void deleteSeller(Long id);

    Seller verifyEmail(String email, String otp);
    @SuppressWarnings("unused")
    Seller updateSellerAccountStatus(Long sellerId, AccountStatus status);
    SellerStatusResponse mapToStatusResponse(Seller seller);
}
