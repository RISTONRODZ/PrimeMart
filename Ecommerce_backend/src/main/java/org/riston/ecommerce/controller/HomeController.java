package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.Home;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.service.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Home Page", description = "Home Page of Prime Mart")
public class HomeController {
    private final HomeService homeService;
    @GetMapping("/home")
    @Operation(summary = "get home page", description = "Retrieves the home page of Prime Mart")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved home page",
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)
            ))
    public ResponseEntity<ApiResponseDto<String>> HomeControllerHandler(){
        return ResponseEntity.ok(ApiResponseDto.success("welcome to PrimeMart",null));
    }

    @GetMapping("/home-page")
    @Operation(summary = "Get home page data", description = "Fetches the full grouped home page data (grid, shop-by-categories, electric categories, deal categories, deals).")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved home page data",
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)
            ))
    public ResponseEntity<ApiResponseDto<Home>> getHomePageData(){
        Home home = homeService.getHomePageData();
        return ResponseEntity.ok(ApiResponseDto.success("Home page data fetched successfully", home));
    }
}