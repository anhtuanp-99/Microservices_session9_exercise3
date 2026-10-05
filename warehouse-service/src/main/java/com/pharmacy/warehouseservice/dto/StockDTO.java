package com.pharmacy.warehouseservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDTO {
    private Long medicineId;
    private String medicineName;
    private Integer availableQuantity;
    private String warehouseLocation;
}