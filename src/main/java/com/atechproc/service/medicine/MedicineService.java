package com.atechproc.service.medicine;

import com.atechproc.dto.CategoryMedicineDto;
import com.atechproc.dto.MedicineDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.enums.MEDICINE_STATUS;
import com.atechproc.enums.MEDICINE_TYPE;
import com.atechproc.exception.ResourceNotFoundException;
import com.atechproc.mapper.MedicineMapper;
import com.atechproc.model.*;
import com.atechproc.repository.MedicineRepository;
import com.atechproc.request.medicine.CreateMedicineRequest;
import com.atechproc.request.medicine.UpdateMedicineRequest;
import com.atechproc.service.category.ICategoryService;
import com.atechproc.service.pharmacy.IPharmacyService;
import com.atechproc.service.supplier.ISupplierService;
import com.atechproc.service.user.IUserService;
import com.atechproc.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService implements IMedicineService {

    private final MedicineRepository medicineRepository;
    private final ICategoryService categoryService;
    private final ISupplierService supplierService;
    private final IPharmacyService pharmacyService;
    private final Utils utils;

    @Override
    public Medicine getMedicineById(Long medicineId) {
        Medicine medicine = medicineRepository.findByIdAndActive(medicineId, true);
        if (medicine == null) {
            throw new ResourceNotFoundException("Medicine not found with id " + medicineId);
        }
        return medicine;
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public MedicineDto createMedicine(CreateMedicineRequest request, Long supplierId, Long categoryId, String jwt)
            throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        if (request.getQuantity() <= 0) {
            throw new Exception("The quantity should be greater that 0");
        }

        if (request.getSellingPrice().compareTo(request.getPurchasePrice()) <= 0) {
            throw new Exception("The selling price must be greater than the purchase price");
        }
        Medicine medicine = medicineRepository.findByNameAndBatchNumberAndPharmacy_id(request.getName(),
                request.getBatchNumber(), pharmacy.getId());

        if (medicine != null) {
            medicine.setActive(true);
        } else
            medicine = new Medicine();

        Category category = categoryService.getCategoryById(categoryId);
        Supplier supplier = supplierService.getSupplierById(supplierId);

        // Medicine newMedicine = new Medicine();
        medicine.setName(request.getName());
        medicine.setQuantity(request.getQuantity());
        medicine.setExpiredDate(request.getExpiredDate());
        medicine.setPurchasePrice(request.getPurchasePrice());
        medicine.setSellingPrice(request.getSellingPrice());
        medicine.setBatchNumber(request.getBatchNumber());
        medicine.setProfit(medicine.calculateProfit());
        medicine.setCategory(category);
        medicine.setSupplier(supplier);
        medicine.setPharmacy(pharmacy);
        medicine.setLow(medicine.updateLowVerification());
        medicine.setType(request.getType());
        medicine.setInStock(medicine.verifyIsInStock());

        Medicine savedMedicine = medicineRepository.save(medicine);

        savedMedicine.setStatus(savedMedicine.updateStatus());

        return MedicineMapper.toDto(medicineRepository.save(savedMedicine));
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public MedicineDto updateMedicine(
            UpdateMedicineRequest request,
            Long supplierId,
            Long categoryId,
            Long id,
            String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Medicine medicine = getMedicineById(id);

        if (medicine.getStatus().equals(MEDICINE_STATUS.EXPIRED)) {
            throw new Exception("You cannot updated this medicine, because it is expired");
        }

        if (!pharmacy.getMedicines().contains(medicine)) {
            throw new Exception("This medicine does not belong to this pharmacy");
        }

        Category category = categoryService.getCategoryById(categoryId);

        if (!category.getMedicines().contains(medicine)) {
            throw new Exception("This medicine does not belong to this category");
        }

        if (request.getQuantity() >= 0) {
            medicine.setQuantity(request.getQuantity());
            medicine.setInStock(medicine.verifyIsInStock());
            medicine.setLow(medicine.updateLowVerification());
        }

        if (request.getName() != null) {
            medicine.setName(request.getName());
        }

        if (request.getBatchNumber() != null) {
            medicine.setBatchNumber(request.getBatchNumber());
        }

        if (request.getExpiredDate() != null) {
            medicine.setExpiredDate(request.getExpiredDate());
            medicine.updateStatus();
        }

        if (request.getPurchasePrice() != null) {
            medicine.setPurchasePrice(request.getPurchasePrice());
            medicine.setProfit(medicine.calculateProfit());
        }

        if (request.getSellingPrice() != null) {
            medicine.setSellingPrice(request.getSellingPrice());
            medicine.setProfit(medicine.calculateProfit());
        }

        if (request.getType() != null) {
            medicine.setType(request.getType());
        }

        if (request.getCategoryId() != null) {
            Category updatedCategory = categoryService.getCategoryById(request.getCategoryId());
            medicine.setCategory(updatedCategory);
        }

        if (request.getSupplierId() != null) {
            Supplier updatedSupplier = supplierService.getSupplierById(request.getSupplierId());
            medicine.setSupplier(updatedSupplier);
        }

        Medicine savedMedicine = medicineRepository.save(medicine);

        return MedicineMapper.toDto(savedMedicine);
    }

    @Override
    public List<MedicineDto> getAllPharmacyMedicines(String jwt) {
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);
        List<Medicine> medicines = medicineRepository.findByPharmacy_idAndActive(pharmacy.getId(), true);
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getMedicinesByStatus(MEDICINE_STATUS status, String jwt) {
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);
        List<Medicine> medicines = medicineRepository.findByStatusAndPharmacy_idAndActive(status, pharmacy.getId(),
                true);
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public MedicineDto updateMedicineStatus(Long id, String jwt, MEDICINE_STATUS status) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Medicine medicine = getMedicineById(id);
        if (!pharmacy.getMedicines().contains(medicine)) {
            throw new Exception("You are not allowed to change this medicine status");
        }

        medicine.setStatus(status);
        Medicine savedMedicine = medicineRepository.save(medicine);

        return MedicineMapper.toDto(savedMedicine);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public void deleteMedicine(Long id, Long categoryId, Long supplierId, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Category category = categoryService.getCategoryById(categoryId);
        Medicine medicine = getMedicineById(id);
        Supplier supplier = supplierService.getSupplierById(supplierId);

        if (!pharmacy.getMedicines().contains(medicine)) {
            throw new Exception("This medicine does not belong to this pharmacy");
        }

        if (!pharmacy.getSuppliers().contains(supplier)) {
            throw new Exception("Bad request");
        }

        if (!supplier.getMedicines().contains(medicine)) {
            throw new Exception("Bad request");
        }

        if (!category.getMedicines().contains(medicine)) {
            throw new Exception("This medicine does not belong to this category");
        }

        medicine.setActive(false);
        medicineRepository.save(medicine);
    }

    @Override
    public MedicineDto getMedicineByBatchNumber(String batchNumber, String medicineName, String jwt) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Medicine medicine = medicineRepository.findByBatchNumberAndNameAndActiveAndPharmacy_id(batchNumber,
                medicineName, true, pharmacy.getId());
        if (medicine == null) {
            throw new ResourceNotFoundException(
                    "Medicine not found with batch number " + batchNumber + " and name " + medicineName);
        }
        return MedicineMapper.toDto(medicine);
    }

    @Override
    public List<MedicineDto> searchForMedicinesByName(String keyword, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        List<Medicine> medicines = medicineRepository.searchForMedicineAndActive(keyword, pharmacy.getId());
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> searchForMedicineByBatchNumber(String keyword, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        List<Medicine> medicines = medicineRepository.searchForMedicineByBatchNumber(keyword, pharmacy.getId());
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getMedicinesByType(MEDICINE_TYPE type, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        List<Medicine> medicines = medicineRepository.findByTypeAndPharmacy_idAndActive(type, pharmacy.getId(), true);

        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getTodayTopSellingMedicines(String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Medicine> medicines = medicineRepository.getCurrentDayTopSellingMedicines(
                pharmacy.getId(), LocalDate.now());
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getCurrentWeekTopSellingMedicines(String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        List<Medicine> medicines = medicineRepository.getCurrentWeekTopSellingMedicines(
                pharmacy.getId(), LocalDate.now().get(WeekFields.ISO.weekOfMonth()), YearMonth.now().toString());
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getCurrentMonthTopSellingMedicines(String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Medicine> medicines = medicineRepository.getCurrentMonthTopSellingMedicines(
                pharmacy.getId(), YearMonth.now().toString());
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getCurrentYearTopSellingMedicines(String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Medicine> medicines = medicineRepository.getCurrentYearTopSellingMedicines(
                pharmacy.getId(), LocalDate.now().getYear());
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getDayTopSellingMedicines(String jwt, LocalDate date) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        List<Medicine> medicines = medicineRepository.getDayTopSellingMedicines(pharmacy.getId(), date);
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getWeekTopSellingMedicines(String jwt, int weekOfMonth, YearMonth yearMonth)
            throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Medicine> medicines = medicineRepository.getWeekTopSellingMedicines(pharmacy.getId(), weekOfMonth,
                yearMonth.toString());
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getMonthTopSellingMedicines(String jwt, YearMonth yearMonth) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        List<Medicine> medicines = medicineRepository.getMonthTopSellingMedicines(pharmacy.getId(),
                yearMonth.toString());
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getYearTopSellingMedicines(String jwt, int year) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Medicine> medicines = medicineRepository.getYearTopSellingMedicines(pharmacy.getId(), year);
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getAvailableMedicines(String jwt) throws Exception {
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);
        List<Medicine> medicines = medicineRepository.findByStatusAndInStockAndPharmacy_idAndActive(
                MEDICINE_STATUS.AVAILABLE, true, pharmacy.getId(), true);
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> searchForMedicinesByGroupAndMedicineName(String jwt, Long groupId, String keyword)
            throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Category category = categoryService.getCategoryById(groupId);
        List<Medicine> medicines = medicineRepository.searchForMedicineByGroupAndName(pharmacy.getId(),
                category.getId(), keyword);
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> searchForMedicinesBySupplierAndMedicineName(String jwt, Long supplierId, String keyword)
            throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Supplier supplier = supplierService.getSupplierById(supplierId);
        List<Medicine> medicines = medicineRepository.searchForMedicineBySupplierAndName(pharmacy.getId(),
                supplier.getId(), keyword);
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> searchForExpiredMedicinesByName(String jwt, String keyword) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Medicine> medicines = medicineRepository.searchFormExpiredMedicinesByName(keyword, pharmacy.getId());
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> searchForMedicinesByNameAndBatchNumber(String jwt, String name, String batchNumber) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Medicine> medicines = medicineRepository.searchByNameAndBatchNumber(pharmacy.getId(), name, batchNumber);
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getInStockMedicines(String jwt) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Medicine> medicines = medicineRepository.findByPharmacy_idAndInStockAndActive(pharmacy.getId(), true, true);
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getLowStockMedicines(String jwt) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Medicine> medicines = medicineRepository.findByPharmacy_idAndLowAndActive(pharmacy.getId(), true, true);
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getOutStockMedicines(String jwt) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Medicine> medicines = medicineRepository.findByPharmacy_idAndInStockAndActive(pharmacy.getId(), false, true);
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getMedicinesByCategory(Long categoryId, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);


        Category category = categoryService.getCategoryById(categoryId);

        if (!pharmacy.getCategories().contains(category)) {
            throw new Exception("You can not update this medicine");
        }

        List<Medicine> medicines = category.getMedicines();
        medicines = medicines.stream()
                .filter(Medicine::isActive).toList();

        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getMedicinesBySupplier(Long supplierId, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Supplier supplier = supplierService.getSupplierById(supplierId);

        if (!pharmacy.getSuppliers().contains(supplier)) {
            throw new Exception("Bad requirest");
        }

        List<Medicine> medicines = medicineRepository.findByPharmacy_idAndSupplier_idAndActive(pharmacy.getId(),
                supplier.getId(), true);

        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public List<MedicineDto> getExpiredMedicines(String jwt) {
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);
        List<Medicine> medicines = medicineRepository.findByStatusAndPharmacy_idAndActive(
                MEDICINE_STATUS.EXPIRED, pharmacy.getId(), true);
        return MedicineMapper.toDTOs(medicines);
    }

    @Override
    public MedicineDto updateMedicineQuantity(Long id, String jwt, int quantity) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Medicine medicine = getMedicineById(id);
        if (!pharmacy.getMedicines().contains(medicine)) {
            throw new Exception("You cant update this medicine quantity");
        }
        medicine.setQuantity(quantity);
        medicine.setLow(medicine.updateLowVerification());
        Medicine savedMedicine = medicineRepository.save(medicine);
        return MedicineMapper.toDto(savedMedicine);
    }
}