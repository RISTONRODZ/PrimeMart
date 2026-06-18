package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.riston.ecommerce.response.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Home Page" , description = "Home Page of Prime Mart")
public class HomeController {
    @GetMapping("/home")
    @Operation(summary = "get home page", description = "Retrieves the home page of Prime Mart")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved home page",
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)
            ))
    public ResponseEntity<ApiResponseDto<String>> HomeControllerHandler(){
       return ResponseEntity.ok(ApiResponseDto.success("welcome to PrimeMart",null));
    }
}
