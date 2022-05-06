package com.tdonuk.bookservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
public class UserDTO {
    private long userId;

    private Name name;

    private Phone primaryPhone;

    private String role;

    private String email;

    private String username;
    private String password;

    private Date birthDate;

    private Date accountCreationDate;
    private Date lastLoginDate;
    private Date lastLogoutDate;
}
