package com.eurder.backend.integration;

import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.reponse.ItemsToShipDto;
import com.eurder.backend.dto.reponse.ItemsToShipListDto;
import com.eurder.backend.dto.reponse.OrderListDto;
import com.eurder.backend.repository.CustomerRepository;
import com.eurder.backend.repository.OrderRepository;
import com.eurder.backend.service.CustomerService;
import com.eurder.backend.service.ItemService;
import com.eurder.backend.util.CustomerUtil;
import com.eurder.backend.util.ItemUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;
import java.util.List;

import static com.eurder.backend.util.CustomerUtil.*;
import static com.eurder.backend.util.ItemUtil.*;
import static io.restassured.http.ContentType.*;
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
        assertThat(orderRepository.findAll()).isEmpty();
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

        assertThat(createdOrderDto.getLocation()).isEqualTo(URI.create("/orders/1"));
        assertThat(createdOrderDto.getId()).isEqualTo(1L);
        assertThat(createdOrderDto.getPrice()).isEqualTo(apple().getPrice().doubleValue());
        assertThat(orderRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Find all")
    void findAll() {
        OrderListDto orderListDto = RestAssured.given()
                .contentType(JSON)
                .auth()
                .basic("customer@customer.local", "customer")
                .port(port)
                .when()
                .get("/orders")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(OrderListDto.class);

        assertThat(orderListDto.getOrders()).isEmpty();
    }

    @Test
    @DisplayName("")
    @Sql(scripts = "classpath:findAllShippingToday.sql")
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
