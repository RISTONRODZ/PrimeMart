package org.riston.ecommerce.service;

import org.riston.ecommerce.modal.User;


public interface UserService {
    User findUserByJwtToken(String jwt);
    User findUserByEmail(String email);
}
