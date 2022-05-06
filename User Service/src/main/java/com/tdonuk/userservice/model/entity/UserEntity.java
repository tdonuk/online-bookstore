package com.tdonuk.userservice.model.entity;

import com.tdonuk.userservice.model.Name;
import com.tdonuk.userservice.model.Phone;
import com.tdonuk.userservice.model.UserRole;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @Embedded
    private Name name;

    @Embedded
    private Phone primaryPhone;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(unique = true)
    private String email;

    private String username;
    private String password;

    private Date birthDate;

    private Date accountCreationDate;
    private Date lastLoginDate;
    private Date lastLogoutDate;
}
