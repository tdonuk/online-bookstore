package com.tdonuk.bookservice.model;

import lombok.Data;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "title", column = @Column(name = "primary_phone_title")) ,
        @AttributeOverride(name = "lastName", column = @Column(name = "primary_phone_number"))
})
public class Phone {
    private String title;
    private String number;
}
