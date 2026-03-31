package com.atechproc.service.category;

import com.atechproc.dto.CategoryDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.exception.AlreadyExistsException;
import com.atechproc.exception.ResourceNotFoundException;
import com.atechproc.mapper.CategoryMapper;
import com.atechproc.model.Category;
import com.atechproc.model.Pharmacy;
import com.atechproc.model.User;
import com.atechproc.repository.CategoryRepository;
import com.atechproc.request.category.CreateCategoryRequest;
import com.atechproc.request.category.UpdateCategoryName;
import com.atechproc.service.business.IBusinessLogicService;
import com.atechproc.service.pharmacy.IPharmacyService;
import com.atechproc.service.user.IUserService;
import com.atechproc.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final IPharmacyService pharmacyService;
    private final IBusinessLogicService businessLogicService;
    private final Utils utils;

    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryRepository.findByIdAndActive(id, true);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found with id " + id);
        }
        return category;
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public CategoryDto createCategory(CreateCategoryRequest request, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Category category = categoryRepository.findByNameAndPharmacy_id(request.getName(), pharmacy.getId());

        if (category != null) {
            category.setActive(true);
        } else
            category = new Category();

        category.setName(request.getName());
        category.setPharmacy(pharmacy);

        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.toDto(savedCategory, businessLogicService);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public CategoryDto updateCategory(UpdateCategoryName request, Long id, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Category category = getCategoryById(id);

        if (!pharmacy.getCategories().contains(category)) {
            throw new Exception("Your are not allowed to update this category");
        }

        category.setName(request.getName());
        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.toDto(savedCategory, businessLogicService);
    }

    @Override
    public List<CategoryDto> getAllCategories(String jwt) {
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);
        List<Category> categories = pharmacy.getCategories();
        categories = categories.stream()
                .filter(Category::isActive).toList();
        return CategoryMapper.toDTOs(categories, businessLogicService);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public void deleteCategory(Long id, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Category category = getCategoryById(id);

        if (!pharmacy.getCategories().contains(category)) {
            throw new Exception("You are not allowed to delete this category");
        }

        category.setActive(false);
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryDto> searchForCategory(String keyword, String jwt) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        List<Category> categories = categoryRepository.searchForCategory(keyword, pharmacy.getId());
        return CategoryMapper.toDTOs(categories, businessLogicService);
    }
}
