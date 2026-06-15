package org.riston.ecommerce.response;

import org.riston.ecommerce.model.SellerReport;
import java.util.Map;

public record SellerReportResponse(
        Long id,
        Map<String, Object> seller,
        long totalEarnings,
        long totalSales,
        long totalRefunds,
        long totalTax,
        long netEarnings,
        Long canceledOrders,
        Long totalOrders
) {
    public SellerReportResponse(SellerReport report) {
        this(
                report.getId(),
                report.getSeller() != null ? Map.of(
                        "id", report.getSeller().getId(),
                        "sellerName", report.getSeller().getSellerName(),
                        "email", report.getSeller().getEmail(),
                        "businessName", report.getSeller().getBusinessDetails() != null ? report.getSeller().getBusinessDetails().getBusinessName() : "N/A",
                        "accountStatus", report.getSeller().getAccountStatus() != null ? report.getSeller().getAccountStatus().toString() : "UNKNOWN"
                ) : Map.of(),
                report.getTotalEarnings(),
                report.getTotalSales(),
                report.getTotalRefunds(),
                report.getTotalTax(),
                report.getNetEarnings(),
                report.getCanceledOrders(),
                report.getTotalOrders()
        );
    }
}