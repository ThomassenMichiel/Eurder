package com.eurder.backend.integration;

import com.eurder.backend.domain.Item;
import com.eurder.backend.dto.reponse.CreatedObjectIdDto;
import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.reponse.ItemDto;
import com.eurder.backend.dto.reponse.ItemDtoList;
import com.eurder.backend.dto.request.*;
import com.eurder.backend.exception.ApiError;
import com.eurder.backend.repository.ItemRepository;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.eurder.backend.util.CustomerUtil.createCustomerDto;
import static com.eurder.backend.util.CustomerUtil.jack;
import static com.eurder.backend.util.ItemUtil.*;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class ItemControllerIntegrationTest {
    @Autowired
    private ItemRepository repository;


    @LocalServerPort
    private int port;
    private String host;


    @BeforeEach
    void setUp() {
        host = "http://localhost:" + port + "/items";
    }

    @Test
    @DisplayName("Save 1 item")
    void postSingleItem() {
        CreatedObjectIdDto answer = postItem(createItemDto(orange()))
                .statusCode(CREATED.value())
                .extract()
                .as(CreatedObjectIdDto.class);

        assertThat(answer).isNotNull();
        assertThat(answer.getId()).isEqualTo(repository.findAll().size());
        assertThat(answer.getLocation().getRawPath()).isEqualTo("/items/2");
    }

    @Test
    @DisplayName("Save multiple items")
    void postMultipleItems() {
        postItem(createItemDto(orange()));
        postItem(createItemDto(apple()));
        postItem(createItemDto(banana()));

        CreatedObjectIdDto answer = postItem(createItemDto(strawberry()))
                .statusCode(CREATED.value())
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
        postItem(itemDto);

        ApiError answer = postItem(itemDto)
                .statusCode(BAD_REQUEST.value())
                .extract()
                .as(ApiError.class);

        assertThat(answer).isNotNull();
        assertThat(answer.getCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(answer.getMessage()).contains("Amount cannot be lower than 0", "Description cannot be blank", "Price cannot be lower than 0", "Name cannot be blank");
        assertThat(answer.getTitle()).isEqualTo("Invalid input");
        assertThat(answer.getStatus()).isEqualTo(BAD_REQUEST.getReasonPhrase());
    }

    @Test
    @DisplayName("Update item")
    void updateItem() {
        postItem(createItemDto(orange()));
        postItem(createItemDto(apple()));
        postItem(createItemDto(banana()));
        postItem(createItemDto(strawberry()));

        Item item = new Item(2L, "Potato", "This is a potato", BigDecimal.valueOf(1337), 200);
        UpdateItemDto updateItemDto = new UpdateItemDto(item.getId(), item.getName(), item.getDescription(), item.getPrice().doubleValue(), item.getAmount());

        putItem(updateItemDto)
                .statusCode(OK.value());

        assertThat(repository.findById(2L)).contains(item);
        assertThat(repository.findAll()).doesNotContain(orange());
    }

    @Test
    @DisplayName("Order an item, update an item and order the same item again with updated values")
    void orderItem_thenUpdateItem_thenOrderTheUpdateItemAgain() {
        CreateOrderDto createOrderDto = new CreateOrderDto(List.of(new CreateItemGroupDto(1L, 1)));
        CreatedOrderDto expectedCreatedOrderDto = new CreatedOrderDto(1L, URI.create("/orders/" + 1L), 2.22);

        CreatedOrderDto createdOrderDto = postOrder(createOrderDto)
                .statusCode(CREATED.value())
                .extract()
                .as(CreatedOrderDto.class);

        assertThat(createdOrderDto).isEqualTo(expectedCreatedOrderDto);

        Item item = new Item(1L, "Potato", "This is a potato", BigDecimal.valueOf(1337), 200);
        UpdateItemDto updateItemDto = new UpdateItemDto(item.getId(), item.getName(), item.getDescription(), item.getPrice().doubleValue(), item.getAmount());

        putItem(updateItemDto)
                .statusCode(OK.value());

        CreateOrderDto updatedCreateOrderDto = new CreateOrderDto(List.of(new CreateItemGroupDto(1L, 10)));
        CreatedOrderDto expectedUpdatedCreatedOrderDto = new CreatedOrderDto(2L, URI.create("/orders/" + 2L), item.getPrice().multiply(BigDecimal.valueOf(updatedCreateOrderDto.getItemGroupList().get(0).getAmount())).doubleValue());

        CreatedOrderDto updatedCreatedOrderDto = postOrder(updatedCreateOrderDto)
                .statusCode(CREATED.value())
                .extract()
                .as(CreatedOrderDto.class);

        assertThat(updatedCreatedOrderDto).isEqualTo(expectedUpdatedCreatedOrderDto);

        assertThat(repository.findById(1L)).contains(item);
        assertThat(repository.findAll()).doesNotContain(orange());
    }

    @Test
    @DisplayName("Find all items sorted by urgency")
    void findAllItemsSortedByUrgency() {
        repository.deleteAll();
        postItem(createItemDto(orange()));
        postItem(createItemDto(apple()));
        postItem(createItemDto(banana()));
        postItem(createItemDto(strawberry()));

        List<ItemDto> itemDtos = new ArrayList<>(List.of(toDto(banana(4L)), toDto(orange(2L)), toDto(strawberry(5L)), toDto(apple(3L))));
        ItemDtoList expected = new ItemDtoList(itemDtos);

        ItemDtoList answer = RestAssured
                .given()
                .auth()
                .preemptive()
                .basic("customer@customer.local", "customer")
                .when()
                .get(host)
                .then()
                .statusCode(OK.value())
                .extract()
                .as(ItemDtoList.class);

        assertThat(answer.getItemList()).hasSize(4);
        assertThat(answer).isEqualTo(expected);
    }

    private ValidatableResponse postItem(CreateItemDto item) {
        return RestAssured.given()
                .auth()
                .preemptive()
                .basic("customer@customer.local", "customer")
                .contentType(JSON)
                .body(item)
                .when()
                .port(port)
                .post(host)
                .then();
    }

    private ValidatableResponse postOrder(CreateOrderDto createOrderDto) {
        return RestAssured.given()
                .contentType(JSON)
                .auth()
                .preemptive()
                .basic("customer@customer.local", "customer")
                .body(createOrderDto)
                .when()
                .port(port)
                .post("http://localhost:" + port + "/orders")
                .then();
    }

    private ValidatableResponse putItem(UpdateItemDto item) {
        return RestAssured.given()
                .auth()
                .basic("customer@customer.local", "customer")
                .contentType(JSON)
                .body(item)
                .when()
                .port(port)
                .put(host)
                .then();
    }

    private ValidatableResponse postCustomer(CreateCustomerDto customer) {
        return RestAssured.given()
                .contentType(JSON)
                .body(customer)
                .when()
                .port(port)
                .post("http://localhost:" + port + "/customers")
                .then();
    }
}
