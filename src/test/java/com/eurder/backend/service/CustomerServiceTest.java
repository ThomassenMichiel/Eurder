package com.eurder.backend.service;

import com.eurder.backend.domain.Customer;
import com.eurder.backend.dto.reponse.CustomerDto;
import com.eurder.backend.dto.request.CreateCustomerDto;
import com.eurder.backend.mapper.CustomerMapper;
import com.eurder.backend.repository.CustomerRepository;
import com.eurder.backend.util.CustomerUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static com.eurder.backend.util.CustomerUtil.john;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private CustomerRepository repository;
    @Mock
    private CustomerMapper mapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private CustomerService service;

    @Test
    @DisplayName("Save a customer")
    void save() {
        Customer customer = john();
        Customer customerAfterCreation = john(10L);
        CreateCustomerDto given = CustomerUtil.createCustomerDto(customer);
        when(repository.save(customer)).thenReturn(customerAfterCreation);
        when(mapper.toDomain(given)).thenReturn(customer);

        Long answer = service.save(given);

        assertThat(answer).isEqualTo(customerAfterCreation.getId());

        verify(mapper).toDomain(any());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Find all customers")
    void findAll() {
        service.findAll();
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find by Id")
    void findById() {
        Long id = 1L;
        CustomerDto expected = CustomerUtil.toDto(john(id));
        when(repository.findById(id)).thenReturn(Optional.of(john(id)));
        when(mapper.toDto(john(id))).thenReturn(expected);

        CustomerDto answer = service.findById(id);

        assertThat(answer).isEqualTo(expected);
    }

    @Test
    @DisplayName("Get current user")
    void getCurrentUser() {
        Customer customer = john(1L);
        when(repository.findCustomerByEmail(customer.getUsername())).thenReturn(Optional.of(customer));
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(customer);

        Customer answer = service.getCurrentUser();

        assertThat(answer).isEqualTo(customer);
    }

}
