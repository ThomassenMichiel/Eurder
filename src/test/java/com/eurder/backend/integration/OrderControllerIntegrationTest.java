package com.eurder.backend.integration;

import com.eurder.backend.controller.OrderController;
import com.eurder.backend.domain.Item;
import com.eurder.backend.domain.ItemGroup;
import com.eurder.backend.dto.reponse.CreatedObjectIdDto;
import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.reponse.OrderDto;
import com.eurder.backend.dto.reponse.OrderListDto;
import com.eurder.backend.dto.request.CreateItemGroupDto;
import com.eurder.backend.dto.request.CreateOrderDto;
import com.eurder.backend.dto.request.UpdateItemDto;
import com.eurder.backend.exception.ApiError;
import com.eurder.backend.mapper.OrderMapper;
import com.eurder.backend.repository.OrderRepository;
import com.eurder.backend.service.CustomerService;
import com.eurder.backend.service.ItemService;
import com.eurder.backend.service.OrderService;
import com.eurder.backend.service.UserService;
import com.eurder.backend.util.CustomerUtil;
import com.eurder.backend.util.ItemUtil;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.eurder.backend.util.CustomerUtil.jack;
import static com.eurder.backend.util.CustomerUtil.joe;
import static com.eurder.backend.util.OrderUtil.*;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderControllerIntegrationTest {
    @Autowired
    private OrderRepository repository;
    @Autowired
    private OrderMapper mapper;
    @Autowired
    private OrderService service;
    @Autowired
    private OrderController controller;
    @Autowired
    private Validator validator;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CustomerService customerService;

    @LocalServerPort
    private int port;
    private String host;


    @BeforeEach
    void setUp() {
        host = "http://localhost:" + port + "/orders";
        itemService.save(ItemUtil.createItemDto(ItemUtil.apple(1L)));
        itemService.save(ItemUtil.createItemDto(ItemUtil.banana(2L)));
        itemService.save(ItemUtil.createItemDto(ItemUtil.strawberry(3L)));
        itemService.save(ItemUtil.createItemDto(ItemUtil.orange(4L)));


        customerService.save(CustomerUtil.createCustomerDto(firstOrder().getCustomer()));
        customerService.save(CustomerUtil.createCustomerDto(secondOrder().getCustomer()));
        customerService.save(CustomerUtil.createCustomerDto(thirdOrder().getCustomer()));
        customerService.save(CustomerUtil.createCustomerDto(fourthOrder().getCustomer()));
    }

    @Test
    @DisplayName("Save 1 order to an empty database")
    void postSingleOrder() {
        assertThat(repository.findAll()).isEmpty();


        CreatedOrderDto answer = post(createOrderDto(firstOrder()))
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(CreatedOrderDto.class);

        assertThat(answer).isNotNull();
        assertThat(answer.getId()).isEqualTo(repository.findAll().size());
        assertThat(answer.getLocation().getRawPath()).isEqualTo("/orders/1");
        assertThat(answer.getPrice()).isEqualTo(firstOrder().getPrice().doubleValue());

        assertThat(itemService.findById(1L).getAmount()).isEqualTo(9);
        for (ItemGroup itemGroup : repository.findById(answer.getId()).get().getItemGroups()) {
            assertThat(itemGroup.getShippingDate()).isEqualTo(LocalDate.now().plusDays(1));
        }
    }

    @Test
    @DisplayName("Save multiple orders")
    void postMultipleOrders() {
        post(createOrderDto(firstOrder()));
        post(createOrderDto(secondOrder()));
        post(createOrderDto(thirdOrder()));

        CreatedOrderDto answer = post(createOrderDto(fourthOrder()))
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(CreatedOrderDto.class);

        assertThat(answer).isNotNull();
        assertThat(answer.getId()).isEqualTo(repository.findAll().size());
        assertThat(answer.getLocation().getRawPath()).isEqualTo("/orders/" + repository.findAll().size());
        assertThat(answer.getPrice()).isEqualTo(fourthOrder().getPrice().doubleValue());

        assertThat(itemService.findById(1L).getAmount()).isEqualTo(9);
        assertThat(itemService.findById(2L).getAmount()).isEqualTo(-20);
        assertThat(itemService.findById(3L).getAmount()).isEqualTo(6);
        assertThat(itemService.findById(4L).getAmount()).isEqualTo(9);

        for (ItemGroup itemGroup : repository.findById(2L).get().getItemGroups()) {
            assertThat(itemGroup.getShippingDate()).isEqualTo(LocalDate.now().plusDays(7));
        }
    }

    @Test
    @DisplayName("Save invalid order")
    void invalidOrder() {
        CreateOrderDto orderDto = new CreateOrderDto(null);
        post(orderDto);

        ApiError answer = post(orderDto)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ApiError.class);

        assertThat(answer).isNotNull();
        assertThat(answer.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(answer.getMessage()).contains("ItemGroupList cannot be null", "ItemGroupList cannot be empty");
        assertThat(answer.getTitle()).isEqualTo("Invalid input");
        assertThat(answer.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @Test
    @DisplayName("Save order with nonexistent item")
    void invalidOrder_nonexistentItem() {
        CreateOrderDto orderDto = new CreateOrderDto(List.of(new CreateItemGroupDto(Long.MAX_VALUE, 10)));
        post(orderDto);

        ApiError answer = post(orderDto)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ApiError.class);

        assertThat(answer).isNotNull();
        assertThat(answer.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(answer.getMessage()).isEqualTo("Item not found");
        assertThat(answer.getTitle()).isEqualTo("Not Found");
        assertThat(answer.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @Test
    @DisplayName("Find all orders for a user")
    void findAllOrdersForCurrentUser() {
        OrderDto firstOrderDto = toDto(firstOrder(1L));
        OrderDto secondOrderDto = toDto(secondOrder(2L));
        post(createOrderDto(firstOrder()));
        post(createOrderDto(secondOrder()));
        post(createOrderDto(thirdOrder()), jack().getEmail(), jack().getPassword());
        post(createOrderDto(fourthOrder()), jack().getEmail(), jack().getPassword());

        OrderListDto expected = new OrderListDto(List.of(firstOrderDto, secondOrderDto), firstOrder().getPrice().add(secondOrder().getPrice()).doubleValue());

        OrderListDto answer = RestAssured.given()
                .contentType(JSON)
                .auth()
                .preemptive()
                .basic(joe().getEmail(), joe().getPassword())
                .when()
                .port(port)
                .get(host)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(OrderListDto.class);

        assertThat(answer).isEqualTo(expected);

    }

    @Test
    @DisplayName("Find all orders for a user - prices of previous orders should not change")
    void findAllOrdersForCurrentUser_pricesOfPreviousOrdersShouldNotChange() {
        OrderDto firstOrderDto = toDto(firstOrder(1L));
        OrderDto secondOrderDto = toDto(secondOrder(2L));
        post(createOrderDto(firstOrder()));
        post(createOrderDto(secondOrder()));
        post(createOrderDto(thirdOrder()), jack().getEmail(), jack().getPassword());
        post(createOrderDto(fourthOrder()), jack().getEmail(), jack().getPassword());

        Item item = new Item(1L, "Potato", "This is a potato", BigDecimal.valueOf(1337), 200);
        UpdateItemDto updateItemDto = new UpdateItemDto(item.getId(), item.getName(), item.getDescription(), item.getPrice().doubleValue(), item.getAmount());

        RestAssured.given()
                .auth()
                .basic("admin", "admin")
                .contentType(JSON)
                .body(updateItemDto)
                .when()
                .port(port)
                .put("http://localhost:" + port + "/items")
                .then()
                .statusCode(HttpStatus.OK.value());

        OrderListDto expected = new OrderListDto(List.of(firstOrderDto, secondOrderDto), firstOrder().getPrice().add(secondOrder().getPrice()).doubleValue());

        OrderListDto answer = RestAssured.given()
                .contentType(JSON)
                .auth()
                .preemptive()
                .basic(joe().getEmail(), joe().getPassword())
                .when()
                .port(port)
                .get(host)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(OrderListDto.class);

        assertThat(answer).isEqualTo(expected);
    }

    private ValidatableResponse post(CreateOrderDto order) {
        return post(order, CustomerUtil.joe(1L).getEmail(), CustomerUtil.joe(1L).getPassword());
    }

    private ValidatableResponse post(CreateOrderDto order, String id, String email) {
        return RestAssured.given()
                .contentType(JSON)
                .auth()
                .preemptive()
                .basic(id, email)
                .body(order)
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
