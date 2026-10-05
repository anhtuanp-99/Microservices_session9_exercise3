package com.pharmacy.pharmacyservice.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResponseDTO {

    private List<BillLineDTO> lines;

    private BigDecimal subtotal;             // Tổng tiền thuốc (chưa VAT)
    private BigDecimal discountAmount;       // Tiền giảm giá (nếu thành viên)
    private BigDecimal vatRate;              // % VAT đang áp dụng
    private BigDecimal vatAmount;            // Tiền VAT
    private BigDecimal totalAmount;          // Tổng cuối cùng
    private String currency;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BillLineDTO {
        private Long medicineId;
        private String medicineName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;
    }
}