package com.eurder.backend.integration;

import com.eurder.backend.controller.ItemController;
import com.eurder.backend.dto.reponse.CreatedObjectIdDto;
import com.eurder.backend.dto.request.CreateItemDto;
import com.eurder.backend.exception.ApiError;
import com.eurder.backend.mapper.ItemMapper;
import com.eurder.backend.repository.ItemRepository;
import com.eurder.backend.service.ItemService;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import static com.eurder.backend.util.ItemUtil.*;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerIntegrationTest {
    @Autowired
    private ItemRepository repository;
    @Autowired
    private ItemMapper mapper;
    @Autowired
    private ItemService service;
    @Autowired
    private ItemController controller;
    @Autowired
    private Validator validator;


    @LocalServerPort
    private int port;
    private String host;


    @BeforeEach
    void setUp() {
        host = "http://localhost:" + port + "/items";
    }

    @Test
    @DisplayName("Save 1 item to an empty database")
    void postSingleItem() {
        assertThat(repository.findAll()).isEmpty();

        CreatedObjectIdDto answer = post(createItemDto(orange()))
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(CreatedObjectIdDto.class);

        assertThat(answer).isNotNull();
        assertThat(answer.getId()).isEqualTo(repository.findAll().size());
        assertThat(answer.getLocation().getRawPath()).isEqualTo("/items/1");
    }

    @Test
    @DisplayName("Save multiple items")
    void postMultipleItems() {
        post(createItemDto(orange()));
        post(createItemDto(apple()));
        post(createItemDto(banana()));

        CreatedObjectIdDto answer = post(createItemDto(strawberry()))
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(CreatedObjectIdDto.class);

        assertThat(answer).isNotNull();
        assertThat(answer.getId()).isEqualTo(repository.findAll().size());
        assertThat(answer.getLocation().getRawPath()).isEqualTo("/items/" + repository.findAll().size());
    }

    @Test
    @DisplayName("Save invalid item")
    void invalidItem() {
        CreateItemDto itemDto = new CreateItemDto("", "", -1, -1);
        post(itemDto);

        ApiError answer = post(itemDto)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ApiError.class);

        assertThat(answer).isNotNull();
        assertThat(answer.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(answer.getMessage()).contains("Amount cannot be lower than 0", "Description cannot be blank", "Price cannot be lower than 0", "Name cannot be blank");
        assertThat(answer.getTitle()).isEqualTo("Invalid input");
        assertThat(answer.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    private ValidatableResponse post(CreateItemDto item) {
        return RestAssured.given()
                .auth()
                .basic("admin", "admin")
                .contentType(JSON)
                .body(item)
                .when()
                .port(port)
                .post(host)
                .then();
    }

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public MethodValidationPostProcessor bean() {
            return new MethodValidationPostProcessor();
        }
    }
}
