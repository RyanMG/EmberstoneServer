package com.emberstone.emberstone_tavern.repository;

import com.emberstone.emberstone_tavern.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeedRepository extends JpaRepository<PersonModel, UUID> {
    @Modifying
    @Query(
            nativeQuery = true,
            value =
"""
DROP TABLE IF EXISTS "person" CASCADE;
DROP TABLE IF EXISTS "army" CASCADE;
DROP TABLE IF EXISTS "campaign" CASCADE;
DROP TABLE IF EXISTS "campaign_person_join" CASCADE;

DROP TYPE IF EXISTS account_status_types CASCADE;

CREATE TYPE account_status_types AS ENUM ('PENDING', 'ACTIVE', 'DELETED');

CREATE TABLE "person" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NOT NULL,
    email varchar(100),
    bio TEXT,
    account_status account_status_types DEFAULT 'PENDING'::account_status_types,
    password TEXT NOT NULL,
    profile_image TEXT
);

CREATE TABLE "army" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    roster TEXT NOT NULL,
    FOREIGN KEY (player_id) REFERENCES person(id) ON DELETE CASCADE
);


CREATE TABLE "campaign" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title varchar(50) NOT NULL,
    description TEXT NOT NULL,
    owner_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "campaign_person_join" (
    id SERIAL PRIMARY KEY,
    player_id UUID NOT NULL,
    campaign_id UUID NOT NULL,
    FOREIGN KEY (player_id) REFERENCES person(id) ON DELETE CASCADE,
    FOREIGN KEY (campaign_id) REFERENCES campaign(id) ON DELETE CASCADE
);
"""
    )
    Void seedDatabase();
}
