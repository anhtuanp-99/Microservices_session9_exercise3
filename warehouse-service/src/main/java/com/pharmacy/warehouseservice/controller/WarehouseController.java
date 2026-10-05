package com.pharmacy.warehouseservice.controller;

import com.pharmacy.warehouseservice.dto.StockDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouse")
@Slf4j
public class WarehouseController {

    /**
     * API kiểm tra tồn kho — GIẢ LẬP chậm 3 giây.
     * Để test Circuit Breaker, có thể:
     *   1. Tắt service hoàn toàn.
     *   2. Giữ nguyên sleep 3s (đủ để timeout nếu có).
     */
    @GetMapping("/stock/{medicineId}")
    public StockDTO checkStock(@PathVariable Long medicineId) throws InterruptedException {
        log.info(">>> Kiểm tra tồn kho cho medicineId={}, mô phỏng chậm 3s...", medicineId);
        Thread.sleep(3000);
        log.info("<<< Kiểm tra xong cho medicineId={}", medicineId);

        return StockDTO.builder()
                .medicineId(medicineId)
                .medicineName("Paracetamol 500mg")
                .availableQuantity(500)
                .warehouseLocation("Kho A - Kệ 3 - Tầng 2")
                .build();
    }
}