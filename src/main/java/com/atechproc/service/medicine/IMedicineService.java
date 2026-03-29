package com.atechproc.service.medicine;

import com.atechproc.dto.CategoryMedicineDto;
import com.atechproc.dto.MedicineDto;
import com.atechproc.enums.MEDICINE_STATUS;
import com.atechproc.enums.MEDICINE_TYPE;
import com.atechproc.model.Medicine;
import com.atechproc.request.medicine.CreateMedicineRequest;
import com.atechproc.request.medicine.UpdateMedicineRequest;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface IMedicineService {
    Medicine getMedicineById(Long medicineId);
    MedicineDto createMedicine(CreateMedicineRequest request, Long supplierId, Long categoryId,  String jwt) throws Exception;
    MedicineDto updateMedicine(UpdateMedicineRequest request, Long supplierId, Long categoryId, Long id, String jwt) throws Exception;
    List<MedicineDto> getMedicinesByStatus(MEDICINE_STATUS status, String jwt);
    MedicineDto updateMedicineStatus(Long id, String jwt, MEDICINE_STATUS status) throws Exception;
    void deleteMedicine(Long id, Long categoryId, Long supplier, String jwt) throws Exception;
    MedicineDto getMedicineByBatchNumber(String batchNumber, String medicineName, String jwt) throws Exception;
    List<MedicineDto> getMedicinesByCategory(Long categoryId, String jwt) throws Exception;
    List<MedicineDto> getMedicinesBySupplier(Long supplierId, String jwt) throws Exception;
    List<MedicineDto> getExpiredMedicines(String jwt);
    MedicineDto updateMedicineQuantity(Long id, String jwt, int quantity) throws Exception;
    List<MedicineDto> searchForMedicinesByName(String keyword, String jwt) throws Exception;
    List<MedicineDto> searchForMedicineByBatchNumber(String keyword, String jwt) throws Exception;
    List<MedicineDto> getMedicinesByType(MEDICINE_TYPE type, String jwt) throws Exception;
    List<MedicineDto> getTodayTopSellingMedicines(String jwt) throws Exception;
    List<MedicineDto> getCurrentWeekTopSellingMedicines(String jwt) throws Exception;
    List<MedicineDto> getCurrentMonthTopSellingMedicines(String jwt) throws Exception;
    List<MedicineDto> getCurrentYearTopSellingMedicines(String jwt) throws Exception;
    List<MedicineDto> getDayTopSellingMedicines(String jwt, LocalDate date) throws Exception;
    List<MedicineDto> getWeekTopSellingMedicines(String jwt, int weekOfMonth, YearMonth yearMonth) throws Exception;
    List<MedicineDto> getMonthTopSellingMedicines(String jwt, YearMonth yearMonth) throws Exception;
    List<MedicineDto> getYearTopSellingMedicines(String jwt, int year) throws Exception;
    List<MedicineDto> getAllPharmacyMedicines(String jwt);
    List<MedicineDto> getAvailableMedicines(String jwt) throws Exception;
    List<MedicineDto> searchForMedicinesByGroupAndMedicineName(String jwt, Long groupId, String keyword) throws Exception;
    List<MedicineDto> searchForMedicinesBySupplierAndMedicineName(String jwt, Long supplierId, String keyword) throws Exception;
    List<MedicineDto> searchForExpiredMedicinesByName(String jwt, String keyword) throws Exception;
}
