package com.eurder.backend.integration;

import com.eurder.backend.domain.Customer;
import com.eurder.backend.dto.reponse.CreatedObjectIdDto;
import com.eurder.backend.dto.reponse.CustomerDto;
import com.eurder.backend.dto.reponse.CustomerListDto;
import com.eurder.backend.dto.request.CreateCustomerDto;
import com.eurder.backend.exception.ApiError;
import com.eurder.backend.repository.CustomerRepository;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.eurder.backend.util.CustomerUtil.*;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class CustomerControllerIntegrationTest {
    @Autowired
    private CustomerRepository repository;

    @LocalServerPort
    private int port;
    private String host;


    @BeforeEach
    void setUp() {
        host = "http://localhost:" + port + "/customers";
    }

    @Test
    @DisplayName("Save multiple customers")
    void postMultipleCustomers() {
        post(createCustomerDto(jack()));
        post(createCustomerDto(john()));
        post(createCustomerDto(joe()));

        CreatedObjectIdDto answer = post(createCustomerDto(bobby()))
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(CreatedObjectIdDto.class);

        assertThat(answer).isNotNull();
        assertThat(answer.getId()).isEqualTo(repository.findAll().size());
        assertThat(answer.getLocation().getRawPath()).isEqualTo("/customers/" + repository.findAll().size());
    }

    @Test
    @DisplayName("Save invalid customer")
    void invalidCustomer() {
        CreateCustomerDto customerDto = new CreateCustomerDto("", "", "", null, "", "password");
        post(customerDto);

        ApiError answer = post(customerDto)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ApiError.class);

        assertThat(answer).isNotNull();
        assertThat(answer.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(answer.getMessage()).contains("Email is not valid", "Address cannot be null", "Last name cannot be empty", "Phonenumber cannot be empty", "First name cannot be empty", "Email cannot be empty");
        assertThat(answer.getTitle()).isEqualTo("Invalid input");
        assertThat(answer.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @Test
    @DisplayName("Find all customers")
    void findAllCustomers() {
        Customer jack = jack();
        Customer john = john();
        Customer joe = joe();
        Customer bobby = bobby();

        CustomerDto jackDto = toDto(jack);
        CustomerDto johnDto = toDto(john);
        CustomerDto joeDto = toDto(joe);
        CustomerDto bobbyDto = toDto(bobby);

        post(createCustomerDto(jack));
        post(createCustomerDto(john));
        post(createCustomerDto(joe));
        post(createCustomerDto(bobby));

        CustomerListDto answer = findAllRestAssuredCall();
        assertThat(answer.getCustomers()).hasSize(repository.findAll().size());
        assertThat(answer.getCustomers()).contains(jackDto, johnDto, joeDto, bobbyDto);
    }

    @Test
    @DisplayName("Find by id")
    void findById() {
        Customer jack = jack(2L);
        Customer john = john(3L);
        Customer joe = joe(4L);
        Customer bobby = bobby(5L);

        CustomerDto jackDto = toDto(jack);
        CustomerDto johnDto = toDto(john);
        CustomerDto joeDto = toDto(joe);
        CustomerDto bobbyDto = toDto(bobby);

        post(createCustomerDto(jack));
        post(createCustomerDto(john));
        post(createCustomerDto(joe));
        post(createCustomerDto(bobby));

        CustomerDto jackAnswerDto = findById(jackDto.getId());
        assertThat(jackAnswerDto).isEqualTo(jackDto);
        CustomerDto johnAnswerDto = findById(johnDto.getId());
        assertThat(johnAnswerDto).isEqualTo(johnDto);
        CustomerDto joeAnswerDto = findById(joeDto.getId());
        assertThat(joeAnswerDto).isEqualTo(joeDto);
        CustomerDto bobbyAnswerDto = findById(bobbyDto.getId());
        assertThat(bobbyAnswerDto).isEqualTo(bobbyDto);
    }

    private CustomerDto findById(Long id) {
        return RestAssured
                .given()
                .auth()
                .preemptive()
                .basic("customer@customer.local", "customer")
                .when()
                .port(port)
                .get(host + "/" + id)
                .then()
                .assertThat()
                .contentType(JSON)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerDto.class);
    }

    private CustomerListDto findAllRestAssuredCall() {
        return RestAssured.given()
                .contentType(JSON)
                .auth()
                .preemptive()
                .basic("customer@customer.local", "customer")
                .when()
                .port(port)
                .get(host)
                .then()
                .assertThat()
                .contentType(JSON)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerListDto.class);
    }

    private ValidatableResponse post(CreateCustomerDto customer) {
        return RestAssured.given()
                .contentType(JSON)
                .body(customer)
                .when()
                .port(port)
                .post(host)
                .then();
    }
}
