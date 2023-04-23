package com.eurder.backend.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "order", fetch = FetchType.EAGER)
    private List<ItemGroup> itemGroups = new ArrayList<>();
    @OneToOne(optional = false)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    public Order() {

    }

    public Order(Long id, List<ItemGroup> itemGroup, Customer customer) {
        this.id = id;
        this.itemGroups = itemGroup;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }

    public BigDecimal getPrice() {
        return itemGroups.stream().map(ItemGroup::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void addItemGroup(ItemGroup itemGroup) {
        this.itemGroups.add(itemGroup);
        itemGroup.setOrder(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id) && Objects.equals(itemGroups, order.itemGroups) && Objects.equals(customer, order.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemGroups, customer);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", itemGroups=" + itemGroups +
                ", customer=" + customer +
                '}';
    }

    public void setCustomer(Customer currentUser) {
        this.customer = currentUser;
    }
}
