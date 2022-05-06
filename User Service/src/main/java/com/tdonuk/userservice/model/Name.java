package com.tdonuk.userservice.model;

import lombok.Data;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "firstname", column = @Column(name = "first_name")) ,
        @AttributeOverride(name = "lastname", column = @Column(name = "last_name"))
})
public class Name {
    private String firstname;
    private String lastname;

    public Name() {
    }

    public Name(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String toString() {
        return firstname + " " + lastname;
    }
}
