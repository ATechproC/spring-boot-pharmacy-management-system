package com.atechproc.controller;

import com.atechproc.dto.CategoryDto;
import com.atechproc.mapper.CategoryMapper;
import com.atechproc.mapper.SupplierMapper;
import com.atechproc.model.Category;
import com.atechproc.model.Supplier;
import com.atechproc.request.category.CreateCategoryRequest;
import com.atechproc.request.category.UpdateCategoryName;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.business.IBusinessLogicService;
import com.atechproc.service.category.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;
    private final IBusinessLogicService businessLogicService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse> createCategoryHandler(
            @RequestHeader("Authorization") String jwt,
            @Valid
            @RequestBody
            CreateCategoryRequest request
    ) throws Exception {
        CategoryDto category = categoryService.createCategory(request, jwt);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Medicine category created successfully", category));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ApiResponse> updateCategory(
            @RequestHeader("Authorization") String jwt,
            @RequestBody
            UpdateCategoryName request,
            @PathVariable() Long id
    ) throws Exception {
        CategoryDto category = categoryService.updateCategory(request, id, jwt);
        return ResponseEntity.ok(new ApiResponse("Category updated successfully", category));
    }

    @GetMapping
    ResponseEntity<ApiResponse> getAllCategoriesHandler(
            @RequestHeader("Authorization") String jwt
    ) {
        List<CategoryDto> categories = categoryService.getAllCategories(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", categories));
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse> getSupplierByIdHandler(@PathVariable Long id) throws Exception {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new ApiResponse("Success", CategoryMapper.toDto(category, businessLogicService)));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<ApiResponse> deleteCategoryHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        categoryService.deleteCategory(id, jwt);
        return ResponseEntity.ok(new ApiResponse("Category deleted successfully", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchForCategoryHandler(
            @RequestParam String keyword,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<CategoryDto> categories = categoryService.searchForCategory(keyword, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", categories));
    }

}
