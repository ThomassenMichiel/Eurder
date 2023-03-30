package com.eurder.backend.dto.request;

import com.eurder.backend.dto.reponse.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;


@Validated
public class CreateCustomerDto {
    @NotNull(message = "First name cannot be null")
    @NotBlank(message = "First name cannot be empty")
    private final String firstName;
    @NotNull(message = "Last name cannot be null")
    @NotBlank(message = "Last name cannot be empty")
    private final String lastName;
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be empty")
    @Pattern(regexp = "^.*@.*\\..*$", message = "Email is not valid")
    private final String email;
    @NotNull(message = "Address cannot be null")
    @Valid
    private final AddressDto address;
    @NotNull(message = "Phonenumber cannot be null")
    @NotBlank(message = "Phonenumber cannot be empty")
    private final String phoneNumber;
    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be empty")
    private final String password;

    public CreateCustomerDto(String firstName, String lastName, String email, AddressDto address, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public AddressDto getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}
