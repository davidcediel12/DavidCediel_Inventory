package com.test.inventory.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "CLIENTS")
public class Client {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true, nullable = false)
    private String identification;

    private String name;
    private String lastname;

    @Lob
    @Column(name = "photo")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] imageData;
}
