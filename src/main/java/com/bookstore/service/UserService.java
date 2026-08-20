package com.bookstore.service;

import com.bookstore.config.DemoAccounts;
import com.bookstore.entity.User;
import com.bookstore.exception.BadRequestException;
import com.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    @Transactional
    public User updateProfile(Long userId, String name, String address, String phone) {
        User user = findById(userId);
        user.setName(name);
        user.setAddress(address);
        user.setPhone(phone);
        return userRepository.save(user);
    }

    /** The demo accounts (admin + student), for display on the login page. No locking — shared freely. */
    public List<DemoAccounts.Account> getDemoAccounts() {
        return DemoAccounts.ALL;
    }
}
