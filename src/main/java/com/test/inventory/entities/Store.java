package com.test.inventory.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "STORES")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    @Column(unique = true, nullable = false)
    private String code;
    @NotNull
    @Size(min = 3)
    private String name;

    @JsonIgnore
    @ManyToMany
    private Set<Product> products;

    public void addProduct(Product product){
        products.add(product);
    }
}
