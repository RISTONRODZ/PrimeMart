package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.Home;
import org.riston.ecommerce.model.HomeCategory;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.response.HomeCategoryDto;
import org.riston.ecommerce.service.HomeCategoryService;
import org.riston.ecommerce.service.HomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home-categories")
public class HomeCategoryController {

    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<Home>> createHomeCategories(@RequestBody List<HomeCategory> homeCategories) {
        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
        Home home = homeService.createHomePageData(categories);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success("Home categories created and page data updated successfully", home));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<HomeCategoryDto>>> getHomeCategories() {
        List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();

        List<HomeCategoryDto> dtos = categories.stream().map(HomeCategoryDto::fromEntity).toList();

        return ResponseEntity.ok(ApiResponseDto.success("Home categories fetched successfully", dtos));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDto<HomeCategoryDto>> updateHomeCategory(@PathVariable Long id, @RequestBody HomeCategory homeCategory) {
        HomeCategory updatedCategory = homeCategoryService.updateHomeCategory(homeCategory, id);
        HomeCategoryDto dto = HomeCategoryDto.fromEntity(updatedCategory);

        return ResponseEntity.ok(ApiResponseDto.success("Home category updated successfully", dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteHomeCategory(@PathVariable Long id) {
        homeCategoryService
                .deleteHomeCategory(id);
        return ResponseEntity.ok(ApiResponseDto.success("Home category deleted successfully", null));
    }
}