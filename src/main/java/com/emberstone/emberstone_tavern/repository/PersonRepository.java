package com.emberstone.emberstone_tavern.repository;

import com.emberstone.emberstone_tavern.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<PersonModel, UUID> {

    @Query("SELECT p FROM PersonModel p WHERE p.email = :email")
    Optional<PersonModel> findByEmail(@Param("email") String email);
}
