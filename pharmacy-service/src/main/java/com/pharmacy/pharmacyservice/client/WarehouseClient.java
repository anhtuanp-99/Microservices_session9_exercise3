package com.pharmacy.pharmacyservice.client;

import com.pharmacy.pharmacyservice.dto.external.StockDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "WAREHOUSE-SERVICE")
public interface WarehouseClient {

    @GetMapping("/api/v1/warehouse/stock/{medicineId}")
    StockDTO checkStock(@PathVariable("medicineId") Long medicineId);
}