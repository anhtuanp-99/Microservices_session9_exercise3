package com.pharmacy.pharmacyservice.client;

import com.pharmacy.pharmacyservice.dto.external.StockDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Gateway bọc lời gọi Warehouse-Service với Circuit Breaker.
 * <p>
 * TẠI SAO TÁCH RIÊNG?
 *   Vì @CircuitBreaker hoạt động qua AOP proxy.
 *   Nếu gọi method cùng class (self-invocation) → proxy bị bypass → CB không chạy.
 * <p>
 * CB name = "warehouseCB" — PHẢI khớp key trong file Git:
 *   resilience4j.circuitbreaker.instances.warehouseCB.*
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseGateway {

    private final WarehouseClient warehouseClient;

    @CircuitBreaker(name = "warehouseCB", fallbackMethod = "warehouseFallback")
    public StockDTO checkStock(Long medicineId) {
        log.info("Gọi Warehouse-Service để kiểm tra tồn kho, medicineId={}", medicineId);
        return warehouseClient.checkStock(medicineId);
    }

    /**
     * Fallback — gọi khi:
     *   1. Mạch OPEN → fail-fast, không gọi Warehouse.
     *   2. Method gốc ném exception → ghi nhận và gọi fallback.
     * <p>
     * Trả về StockDTO với availableQuantity=0 để báo "chưa xác nhận được tồn kho".
     * Đây là graceful degradation — máy tính tiền vẫn hoạt động.
     */
    public StockDTO warehouseFallback(Long medicineId, Throwable throwable) {
        log.warn(">>> FALLBACK Warehouse: medicineId={}, nguyên nhân={}",
                medicineId, throwable.getClass().getSimpleName());

        return StockDTO.builder()
                .medicineId(medicineId)
                .medicineName("N/A")
                .availableQuantity(0)
                .warehouseLocation("Không xác định — Kho tổng đang gặp sự cố")
                .build();
    }
}