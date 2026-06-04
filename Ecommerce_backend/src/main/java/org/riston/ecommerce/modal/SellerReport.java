package org.riston.ecommerce.modal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private  Seller seller;

    private Long totalEarnings = 0L;

    private Long totalSales = 0L;

    private Long totalRefunds = 0L;

    private Long totalTax = 0L;

    private Long netEarnings = 0L;

}
