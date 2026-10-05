package com.pharmacy.pharmacyservice.controller;

import com.pharmacy.pharmacyservice.client.WarehouseGateway;
import com.pharmacy.pharmacyservice.dto.external.StockDTO;
import com.pharmacy.pharmacyservice.dto.request.BillItemDTO;
import com.pharmacy.pharmacyservice.dto.request.BillRequestDTO;
import com.pharmacy.pharmacyservice.dto.response.BillResponseDTO;
import com.pharmacy.pharmacyservice.entity.Medicine;
import com.pharmacy.pharmacyservice.exception.ResourceNotFoundException;
import com.pharmacy.pharmacyservice.repository.MedicineRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bill")
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class BillController {

    private final MedicineRepository medicineRepository;
    private final WarehouseGateway warehouseGateway;

    @Value("${pharmacy.vat-rate}")
    private BigDecimal vatRate;

    @Value("${pharmacy.currency:VND}")
    private String currency;

    @Value("${pharmacy.discount-member:0}")
    private BigDecimal memberDiscount;

    @PostMapping
    public ResponseEntity<BillResponseDTO> calculateBill(
            @Valid @RequestBody BillRequestDTO request) {

        log.info("Tính hóa đơn — VAT rate hiện tại: {}", vatRate);

        List<BillResponseDTO.BillLineDTO> lines = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (BillItemDTO item : request.getItems()) {
            Medicine medicine = medicineRepository.findById(item.getMedicineId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Medicine", "ID", item.getMedicineId()));

            // ===== GỌI WAREHOUSE ĐỂ KIỂM TRA TỒN KHO (có Circuit Breaker) =====
            StockDTO stock = warehouseGateway.checkStock(item.getMedicineId());
            log.info("Tồn kho medicineId={}: available={}, location={}",
                    item.getMedicineId(), stock.getAvailableQuantity(), stock.getWarehouseLocation());

            // Nếu kho không xác nhận được → cảnh báo nhưng vẫn bán
            if (stock.getAvailableQuantity() == 0) {
                log.warn("CẢNH BÁO: Không xác nhận được tồn kho cho medicineId={}", item.getMedicineId());
            }

            BigDecimal lineTotal = medicine.getUnitPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            lines.add(BillResponseDTO.BillLineDTO.builder()
                    .medicineId(medicine.getId())
                    .medicineName(medicine.getName())
                    .quantity(item.getQuantity())
                    .unitPrice(medicine.getUnitPrice())
                    .lineTotal(lineTotal)
                    .build());

            subtotal = subtotal.add(lineTotal);
        }

        boolean isMember = Boolean.TRUE.equals(request.getMember());
        BigDecimal discountAmount = isMember
                ? subtotal.multiply(memberDiscount).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal afterDiscount = subtotal.subtract(discountAmount);
        BigDecimal vatAmount = afterDiscount.multiply(vatRate)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalAmount = afterDiscount.add(vatAmount);

        BillResponseDTO response = BillResponseDTO.builder()
                .lines(lines)
                .subtotal(subtotal)
                .discountAmount(discountAmount)
                .vatRate(vatRate)
                .vatAmount(vatAmount)
                .totalAmount(totalAmount)
                .currency(currency)
                .build();

        log.info("Hóa đơn — subtotal={}, VAT={}, total={}",
                subtotal, vatAmount, totalAmount);

        return ResponseEntity.ok(response);
    }
}