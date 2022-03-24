package com.test.inventory.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Lob;

@Getter
@Setter
@ToString
public class ClientDto {

    @NotNull
    @Length(min = 3)
    private String identification;
    @Length(min = 3)
    @NotNull
    private String name;
    @Length(min = 3)
    @NotNull
    private String lastname;

    @JsonIgnore
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] imageData;
}
