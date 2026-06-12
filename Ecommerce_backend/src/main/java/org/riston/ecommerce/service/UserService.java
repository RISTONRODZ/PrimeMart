package org.riston.ecommerce.service;

import org.riston.ecommerce.model.User;


public interface UserService {
    User findUserByJwtToken(String jwt);
    User findUserByEmail(String email);
}
