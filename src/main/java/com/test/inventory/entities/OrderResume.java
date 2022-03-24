package com.test.inventory.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "ORDER_RESUME")
@Getter
@Setter
public class OrderResume {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(optional = false)
    private Client client;

    @OneToMany(mappedBy = "orderResume")
    private Set<Order> orders;

    private LocalDateTime dateTime;
}