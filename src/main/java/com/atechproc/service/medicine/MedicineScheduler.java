package com.atechproc.service.medicine;

import com.atechproc.enums.MEDICINE_STATUS;
import com.atechproc.model.Medicine;
import com.atechproc.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class MedicineScheduler {

    private final MedicineRepository medicineRepository;

    @Scheduled(cron = "0 0 0 * * *") // runs every midnight
    public void updateExpiredMedicines() {
        medicineRepository.markExpiredMedicines(YearMonth.now());
    }
}