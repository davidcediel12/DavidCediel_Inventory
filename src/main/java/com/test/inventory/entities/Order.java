package com.test.inventory.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(optional = false)
    private Store store;

    @ManyToOne(optional = false)
    private Client client;

    private LocalDateTime dateTime;

    @ManyToOne(optional = false)
    private Product product;

    @Column(updatable = false)
    private Integer items;

    @Column(updatable = false)
    private Double totalPrice;

}
