package com.example.pos.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.pos.model.DetailRevenue;
import com.example.pos.model.RevenueSummary;
import com.example.pos.repository.DetailRevenueRepository;
import com.example.pos.repository.RevenueSummaryRepository;

import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class RevenueEventListener {

    @Autowired
    private RevenueSummaryRepository revenueSummaryRepository;

    @EventListener
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        BigDecimal totalAmount = event.getTotalAmount();
        String paymentId = event.getPaymentId();

        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Tìm RevenueSummary theo ngày hiện tại, nếu không có thì tạo mới
        RevenueSummary revenueSummary = revenueSummaryRepository.findByReportDate(today)
                .orElse(new RevenueSummary(0, BigDecimal.ZERO, today));

        // Cập nhật số đơn hàng và tổng doanh thu của ngày
        revenueSummary.setTotalOrder(revenueSummary.getTotalOrder() + 1);
        revenueSummary.setTotalRevenue(revenueSummary.getTotalRevenue().add(totalAmount));

        Optional<DetailRevenue> optionalDetail = revenueSummary.getDetailRevenues().stream()
                .filter(dr -> paymentId.equals(dr.getPaymentId()))
                .findFirst();

        if (optionalDetail.isPresent()) {
            // Nếu đã tồn tại, cập nhật số đơn hàng và tổng doanh thu của bản ghi đó
            DetailRevenue existingDetail = optionalDetail.get();
            existingDetail.setTotalOrder(existingDetail.getTotalOrder() + 1);
            existingDetail.setTotalSale(existingDetail.getTotalSale().add(totalAmount));
        } else {
            // Nếu không tồn tại, tạo mới một bản ghi DetailRevenue
            DetailRevenue detailRevenue = new DetailRevenue();
            detailRevenue.setTotalOrder(1);
            detailRevenue.setPaymentId(paymentId);
            detailRevenue.setTotalSale(totalAmount);
            detailRevenue.setReportDate(LocalDateTime.now());

            // Liên kết DetailRevenue với RevenueSummary
            revenueSummary.addDetailRevenue(detailRevenue);
        }
        revenueSummaryRepository.save(revenueSummary);
    }
}