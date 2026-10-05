package com.pharmacy.pharmacyservice.dto.external;

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