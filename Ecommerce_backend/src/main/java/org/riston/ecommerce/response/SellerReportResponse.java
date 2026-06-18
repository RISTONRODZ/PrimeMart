package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.riston.ecommerce.model.SellerReport;
import java.util.Map;
@Schema(description = "Response payload containing the seller's financial and performance report")
public record SellerReportResponse(
        @Schema(description = "Unique ID of the report", example = "1")
        Long id,

        @Schema(description = "Summary of seller details", example = "{ \"id\": 1, \"sellerName\": \"Nike Store\", \"accountStatus\": \"ACTIVE\" }")
        Map<String, Object> seller,

        @Schema(description = "Total revenue earned", example = "50000")
        long totalEarnings,

        @Schema(description = "Total number of sales", example = "150")
        long totalSales,

        @Schema(description = "Total amount refunded", example = "2000")
        long totalRefunds,

        @Schema(description = "Total tax accumulated", example = "5000")
        long totalTax,

        @Schema(description = "Net earnings after deductions", example = "43000")
        long netEarnings,

        @Schema(description = "Total number of canceled orders", example = "5")
        Long canceledOrders,

        @Schema(description = "Total number of orders placed", example = "155")
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