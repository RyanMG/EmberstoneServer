package com.emberstone.emberstone_tavern.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class UserModel {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String password;
}
