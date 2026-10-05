package com.pharmacy.pharmacyservice.dto.response;

import lombok.*;

/**
 * DTO chứa thông tin chi nhánh lấy từ Config Server.
 * Dùng để trả về cho client khi gọi /api/v1/pharmacy/branch-info.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchInfoDTO {
    private String branchName;
    private String hotline;
    private String address;
    private String openTime;
    private String closeTime;
}