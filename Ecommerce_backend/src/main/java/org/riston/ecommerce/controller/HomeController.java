package org.riston.ecommerce.controller;

import org.riston.ecommerce.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("")
    public ResponseEntity<ApiResponse<String>> HomeControllerHandler(){
       return ResponseEntity.ok(ApiResponse.success("welcome to PrimeMart",null));
    }
}
