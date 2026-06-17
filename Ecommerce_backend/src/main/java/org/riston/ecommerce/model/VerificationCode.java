package org.riston.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.riston.ecommerce.domain.USER_ROLE;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String otp;

    private String email;

    @OneToOne
    private User user;

    private USER_ROLE role;

    @OneToOne
    private Seller seller;

    private LocalDateTime expiryDate;
}
