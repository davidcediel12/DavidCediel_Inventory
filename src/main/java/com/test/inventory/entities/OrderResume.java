package com.test.inventory.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ORDER_RESUME")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResume {
    @Id
    @GeneratedValue
    private Integer id;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Client client;

    @OneToMany(mappedBy = "orderResume")
    private Set<Order> orders;

    private LocalDateTime dateTime;

    public void addOrder(Order order){
        if(orders == null)
            orders = new HashSet<>();
        orders.add(order);
    }
}