package com.test.inventory.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "STORES")
public class Store {
    @Id
    @GeneratedValue
    private Integer store;
    @NotNull
    @Column(unique = true, nullable = false)
    private String code;
    @NotNull
    @Size(min = 3)
    private String name;

    @ManyToMany
    /*@JoinTable(name = "Store_Product")/*, joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "id"))*/
    private Set<Product> products;
}
