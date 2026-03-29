package com.atechproc.service.category;

import com.atechproc.dto.CategoryDto;
import com.atechproc.model.Category;
import com.atechproc.request.category.CreateCategoryRequest;
import com.atechproc.request.category.UpdateCategoryName;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    CategoryDto createCategory(CreateCategoryRequest request, String jwt) throws Exception;
    CategoryDto updateCategory(UpdateCategoryName request, Long id, String jwt) throws Exception;
    List<CategoryDto> getAllCategories(String jwt);
    void deleteCategory(Long id, String jwt) throws Exception;
    List<CategoryDto> searchForCategory(String keyword, String jwt) throws Exception;
}
