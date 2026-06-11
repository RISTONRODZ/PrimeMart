package org.riston.ecommerce.repository;

import org.riston.ecommerce.modal.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {
    VerificationCode findByEmail(String Email);
    VerificationCode findByOtp(String otp);
}
