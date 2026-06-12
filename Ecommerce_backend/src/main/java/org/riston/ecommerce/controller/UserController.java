package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/profile")
    public ResponseEntity<User> getUserHandler(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(user);
    }

}
