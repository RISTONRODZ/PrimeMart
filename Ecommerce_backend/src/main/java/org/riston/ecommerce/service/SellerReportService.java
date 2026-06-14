package org.riston.ecommerce.service;

import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
