package com.test.inventory.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue
    private Integer id;

    @JsonProperty("cod")
    @NotNull
    @Column(unique = true, nullable = false)
    private String code;

    @NotNull
    @Column(nullable = false)
    private String name;

    private Double price;

    private Integer stock;


    @ManyToMany(mappedBy = "products")
    private Set<Store> stores;
}
