package com.pharmacy.pharmacyservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineRequestDTO {

    @NotBlank(message = "Tên thuốc không được để trống")
    @Size(max = 200, message = "Tên thuốc không vượt quá 200 ký tự")
    private String name;

    @NotBlank(message = "Nhà sản xuất không được để trống")
    private String manufacturer;

    @NotNull(message = "Đơn giá không được để trống")
    @Min(value = 1, message = "Đơn giá phải lớn hơn 0")
    private BigDecimal unitPrice;

    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Số lượng không được âm")
    private Integer stockQuantity;

    @Future(message = "Ngày hết hạn phải ở tương lai")
    private LocalDate expiryDate;

    @Size(max = 1000, message = "Mô tả không vượt quá 1000 ký tự")
    private String description;
}