package com.emberstone.emberstone_tavern.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
@Table(name="person")
public class PersonModel {
    public enum UserStatus {
        PENDING, ACTIVE, DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private String profileImage;

     @Enumerated(EnumType.STRING)
     private UserStatus accountStatus;
}
