package com.test.inventory.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Lob;

@Getter
@Setter
@ToString
public class ClientDto {
    private String identification;
    private String name;
    private String lastname;
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] imageData;
}
