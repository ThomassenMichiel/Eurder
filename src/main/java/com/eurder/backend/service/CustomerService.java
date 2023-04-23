package com.eurder.backend.service;

import com.eurder.backend.domain.Customer;
import com.eurder.backend.dto.reponse.CustomerDto;
import com.eurder.backend.dto.reponse.CustomerListDto;
import com.eurder.backend.dto.request.CreateCustomerDto;
import com.eurder.backend.exception.CustomerNotFoundException;
import com.eurder.backend.mapper.CustomerMapper;
import com.eurder.backend.repository.CustomerRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService implements UserDetailsService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
//    private final UserService userService;

    public CustomerService(CustomerRepository repository, CustomerMapper mapper/*, UserService userService*/) {
        this.repository = repository;
        this.mapper = mapper;
//        this.userService = userService;
    }

    public Long save(CreateCustomerDto createCustomerDto) {
        Customer createdCustomer = repository.save(mapper.toDomain(createCustomerDto));
//        userService.save(createdCustomer);
        return createdCustomer.getId();
    }

    public Customer getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return repository.findCustomerByEmail(username)
                .orElseThrow(CustomerNotFoundException::new);
    }

    public CustomerListDto findAll() {
        return new CustomerListDto(repository.findAll().stream().map(mapper::toDto).toList());
    }

    public CustomerDto findById(Long id) {
        Customer customer = repository.findById(id).orElseThrow(CustomerNotFoundException::new);
        return mapper.toDto(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        repository.findAll().stream().forEach(System.out::println);
        return repository.findCustomerByEmail(username).orElseThrow(CustomerNotFoundException::new);
    }
}
