package com.test.inventory.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "ORDERS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private OrderResume orderResume;

    @ManyToOne(optional = false)
    private Store store;

    @ManyToOne(optional = false)
    private Product product;

    @Column(updatable = false)
    private Integer items;

    @Column(updatable = false)
    private Double totalPrice;

}
