package org.riston.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.config.JwtProvider;
import org.riston.ecommerce.exception.UserNotFoundException;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.repository.UserRepository;
import org.riston.ecommerce.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException("User not found with email: " + email);
        }
        return user;
    }


}
