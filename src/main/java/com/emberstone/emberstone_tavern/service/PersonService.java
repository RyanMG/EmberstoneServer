package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    /**
     * Gets the user data for the person using the application
     */
    public Optional<PersonModel> getActivePersonByEmail(String email) {
        try {
            return personRepository.findByEmail(email);

        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to get person by email: " + e.getMessage());
        }
    }
}
