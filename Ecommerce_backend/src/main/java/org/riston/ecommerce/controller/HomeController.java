package org.riston.ecommerce.controller;

import org.riston.ecommerce.response.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HomeController {
    @GetMapping("/home")
    public ResponseEntity<ApiResponseDto<String>> HomeControllerHandler(){
       return ResponseEntity.ok(ApiResponseDto.success("welcome to PrimeMart",null));
    }
}
