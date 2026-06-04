package org.riston.ecommerce.modal;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String discountPercentage;

    private LocalDate validityStartDate;

    private LocalDate validityEndDate;
    private  double minimumOrderValue;

    private boolean isActive = true;
@ManyToMany(mappedBy = "usedCoupons")
    private Set<User> usedByUsers = new HashSet<>();
}
