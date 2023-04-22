package com.eurder.backend.service;

import com.eurder.backend.domain.Customer;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public UserService(InMemoryUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(Customer customer) {
        UserDetails user = User.withUsername(customer.getEmail())
                .password(passwordEncoder.encode(customer.getPassword()))
                .roles("USER").build();
        userDetailsManager.createUser(user);
    }
}
