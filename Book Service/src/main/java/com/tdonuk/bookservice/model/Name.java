package com.tdonuk.bookservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Embeddable
public class Name implements Serializable {
    private String firstname;
    private String lastname;
}
