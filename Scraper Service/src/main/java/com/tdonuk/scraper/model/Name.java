package com.tdonuk.scraper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Name implements Serializable {
    private String firstname;
    private String lastname;
}
