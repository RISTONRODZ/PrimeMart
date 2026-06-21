package org.riston.ecommerce.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.SellerReport;
import org.riston.ecommerce.repository.SellerReportRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerReportServiceImplTest {

    @Mock
    private SellerReportRepository sellerReportRepository;

    @InjectMocks
    private SellerReportServiceImpl sellerReportService;

    @Test
    void getSellerReport_WhenReportExists() {
        Seller seller = new Seller();
        seller.setId(1L);
        SellerReport existingReport = new SellerReport();

        when(sellerReportRepository.findBySellerId(1L)).thenReturn(existingReport);

        SellerReport result = sellerReportService.getSellerReport(seller);

        assertEquals(existingReport, result);
        verify(sellerReportRepository, times(1)).findBySellerId(1L);
        verify(sellerReportRepository, never()).save(any(SellerReport.class));
    }

    @Test
    void getSellerReport_WhenReportDoesNotExist() {
        Seller seller = new Seller();
        seller.setId(1L);

        when(sellerReportRepository.findBySellerId(1L)).thenReturn(null);
        when(sellerReportRepository.save(any(SellerReport.class))).thenAnswer(i -> i.getArguments()[0]);

        SellerReport result = sellerReportService.getSellerReport(seller);

        assertEquals(seller, result.getSeller());
        verify(sellerReportRepository, times(1)).findBySellerId(1L);
        verify(sellerReportRepository, times(1)).save(any(SellerReport.class));
    }

    @Test
    void updateSellerReport() {
        SellerReport report = new SellerReport();
        when(sellerReportRepository.save(report)).thenReturn(report);

        SellerReport result = sellerReportService.updateSellerReport(report);

        assertEquals(report, result);
        verify(sellerReportRepository, times(1)).save(report);
    }
}