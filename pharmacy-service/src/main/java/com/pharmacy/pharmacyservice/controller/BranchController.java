package com.pharmacy.pharmacyservice.controller;

import com.pharmacy.pharmacyservice.dto.response.BranchInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pharmacy")
@Slf4j
public class BranchController {

    /**
     * Các giá trị này được nạp từ Git repo qua Config Server.
     * @Value đọc từ Environment — không cần khai báo trong application.yml local.
     */
    @Value("${app.branch-name}")
    private String branchName;

    @Value("${app.hotline}")
    private String hotline;

    @Value("${app.address}")
    private String address;

    @Value("${app.open-time}")
    private String openTime;

    @Value("${app.close-time}")
    private String closeTime;

    /**
     * In thông tin chi nhánh ra console khi khởi động.
     * Dùng @PostConstruct để chạy sau khi Spring inject @Value xong.
     */
    @jakarta.annotation.PostConstruct
    public void printBranchInfo() {
        log.info("=========================================");
        log.info("  KHỞI ĐỘNG PHARMACY SERVICE");
        log.info("  Chi nhánh : {}", branchName);
        log.info("  Hotline   : {}", hotline);
        log.info("  Địa chỉ   : {}", address);
        log.info("  Giờ mở   : {}", openTime);
        log.info("  Giờ đóng  : {}", closeTime);
        log.info("=========================================");
    }

    @GetMapping("/branch-info")
    public ResponseEntity<BranchInfoDTO> getBranchInfo() {
        BranchInfoDTO info = BranchInfoDTO.builder()
                .branchName(branchName)
                .hotline(hotline)
                .address(address)
                .openTime(openTime)
                .closeTime(closeTime)
                .build();
        return ResponseEntity.ok(info);
    }
}