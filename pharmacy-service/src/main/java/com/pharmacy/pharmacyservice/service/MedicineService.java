package com.pharmacy.pharmacyservice.service;

import com.pharmacy.pharmacyservice.dto.request.MedicineRequestDTO;
import com.pharmacy.pharmacyservice.dto.response.MedicineResponseDTO;
import com.pharmacy.pharmacyservice.entity.Medicine;
import com.pharmacy.pharmacyservice.exception.ResourceNotFoundException;
import com.pharmacy.pharmacyservice.mapper.MedicineMapper;
import com.pharmacy.pharmacyservice.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    public MedicineResponseDTO createMedicine(MedicineRequestDTO request) {
        Medicine medicine = Medicine.builder()
                .name(request.getName())
                .manufacturer(request.getManufacturer())
                .unitPrice(request.getUnitPrice())
                .stockQuantity(request.getStockQuantity())
                .expiryDate(request.getExpiryDate())
                .description(request.getDescription())
                .build();

        Medicine saved = medicineRepository.save(medicine);
        log.info("Đã tạo medicine id={}, name={}", saved.getId(), saved.getName());
        return medicineMapper.toResponseDTO(saved);
    }

    public MedicineResponseDTO getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", "ID", id));
        return medicineMapper.toResponseDTO(medicine);
    }

    public List<MedicineResponseDTO> getAllMedicines() {
        return medicineMapper.toResponseDTOList(medicineRepository.findAll());
    }
}