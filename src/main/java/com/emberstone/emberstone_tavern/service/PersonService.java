package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.controllers.AuthController;
import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private Boolean validatePersonData(PersonModel personModel) {
        String email = personModel.getEmail();
        String password = personModel.getPassword();
        String firstName = personModel.getFirstName();
        String lastName = personModel.getLastName();
        boolean validEmail = Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email);
        boolean validPassword = password != null && password.length() >= 6;

        return validEmail && validPassword && firstName != null && lastName != null;
    }
    /**
     * Register a new person
     */
    public HttpResponseModel<String> registerNewPerson(PersonModel personModel) {
        try {
            Boolean isValidUser = validatePersonData(personModel);

            if (isValidUser) {
                Optional<PersonModel> persistedPerson = personRepository.findByEmail(personModel.getEmail());
                if (persistedPerson.isPresent()) {
                    return HttpResponseModel.success("User already exists");
                }

                PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                personModel.setPassword(encoder.encode(personModel.getPassword()));

                personModel.setAccountStatus(PersonModel.AccountStatus.PENDING);

                personRepository.save(personModel);
                return HttpResponseModel.success("User created");
            }

            return HttpResponseModel.error("Invalid user data");

        } catch (Exception err) {
            log.error(err.getMessage());
            return HttpResponseModel.error("Error registering new person");
        }
    }

    public Optional<PersonModel> getActivePersonById(UUID id) {
        try {
            return personRepository.findById(id);

        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to get person by id: " + e.getMessage());
        }
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
