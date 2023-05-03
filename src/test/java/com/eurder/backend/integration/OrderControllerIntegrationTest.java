package com.eurder.backend.integration;

import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.reponse.OrderDto;
import com.eurder.backend.dto.reponse.OrderListDto;
import com.eurder.backend.repository.OrderRepository;
import com.eurder.backend.service.CustomerService;
import com.eurder.backend.service.ItemService;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.List;

import static com.eurder.backend.util.ItemUtil.apple;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class OrderControllerIntegrationTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrderRepository orderRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Save order")
    void saveOrder() {
        String order = """
                {
                    "itemGroupList": [
                        {
                            "itemId": 1,
                            "amount": 1
                        }
                    ]
                }""";
        CreatedOrderDto createdOrderDto = RestAssured.given()
                .contentType(JSON)
                .auth()
                .preemptive()
                .basic("customer@customer.local", "customer")
                .body(order)
                .when()
                .post("http://localhost:" + port + "/orders")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(CreatedOrderDto.class);

        assertThat(createdOrderDto.getLocation()).isEqualTo(URI.create("/orders/2"));
        assertThat(createdOrderDto.getId()).isEqualTo(2L);
        assertThat(createdOrderDto.getPrice()).isEqualTo(apple().getPrice().doubleValue());
        assertThat(orderRepository.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("Reorder a previous order")
    void reorder() {
        List<OrderDto> orders = RestAssured.given()
                .contentType(JSON)
                .auth()
                .preemptive()
                .basic("customer@customer.local", "customer")
                .when()
                .get("http://localhost:" + port + "/orders")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(OrderListDto.class)
                .getOrders();
        Long lastOrderBeforeReorderingId = orders.get(orders.size() - 1).getId();
        String answer = RestAssured.given()
                .contentType(JSON)
                .auth()
                .preemptive()
                .basic("customer@customer.local", "customer")
                .when()
                .post("http://localhost:" + port + "/orders/reorder/1")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.CREATED.value())
                .extract().asString();

        assertThat(answer).isEqualTo(String.format("{\"id\":%d,\"location\":\"/orders/%d\",\"price\":2.22}", ++lastOrderBeforeReorderingId, lastOrderBeforeReorderingId));
    }

    @Test
    @DisplayName("Find all")
    void findAll() {
        String answer = RestAssured.given()
                .contentType(JSON)
                .auth()
                .basic("customer@customer.local", "customer")
                .port(port)
                .when()
                .get("/orders")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .asString();

        assertThat(answer).isEqualTo("{\"orders\":[{\"id\":1,\"itemGroups\":[{\"itemName\":\"apple\",\"orderedAmount\":1,\"price\":2.22}],\"price\":2.22}],\"totalPrice\":2.22}");
    }

    @Test
    @DisplayName("Find all shipping today")
    void findAllShippingToday() {
        String answer = RestAssured.given()
                .contentType(JSON)
                .auth()
                .basic("customer@customer.local", "customer")
                .port(port)
                .when()
                .get("/orders/shipping")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .asString();

        assertThat(answer).isEqualTo("{\"items\":[{\"items\":[{\"itemName\":\"apple\",\"orderedAmount\":1,\"price\":2.22}],\"address\":{\"street\":\"cantersteen\",\"number\":\"14\",\"zipcode\":\"1337\",\"city\":\"brussels\"}}]}");
    }
}
