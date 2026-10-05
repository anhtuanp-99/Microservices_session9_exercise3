package com.pharmacy.pharmacyservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillRequestDTO {

    @NotEmpty(message = "Hóa đơn phải có ít nhất 1 loại thuốc")
    @Valid
    private List<BillItemDTO> items;

    private Boolean member;   // Có phải thành viên không (để áp dụng giảm giá)
}