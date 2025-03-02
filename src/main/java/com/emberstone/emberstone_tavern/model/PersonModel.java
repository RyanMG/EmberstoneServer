package com.emberstone.emberstone_tavern.model;

import com.emberstone.emberstone_tavern.model.roster.RosterModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.UUID;

@Data
@Entity
@Table(name="person")
public class PersonModel {
    public enum AccountStatus {
        PENDING, ACTIVE, DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String bio;

    @Column(name = "profile_image")
    private String profileImage;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private AccountStatus accountStatus;

    @Transient
    private RosterModel roster;
}
