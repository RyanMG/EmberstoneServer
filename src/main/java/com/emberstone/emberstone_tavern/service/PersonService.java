package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.controllers.AuthController;
import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.model.UserModel;
import com.emberstone.emberstone_tavern.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private Boolean validatePersonData(UserModel userModel) {
        String email = userModel.getEmail();
        String password = userModel.getPassword();
        String firstName = userModel.getFirstName();
        String lastName = userModel.getLastName();
        boolean validEmail = Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email);
        boolean validPassword = password != null && password.length() >= 6;

        return validEmail && validPassword && firstName != null && lastName != null;
    }
    /**
     * Register a new person
     */
    public HttpResponseModel<String> registerNewPerson(UserModel userModel) {
        try {
            Boolean isValidUser = validatePersonData(userModel);

            if (isValidUser) {
                Optional<PersonModel> persistedPerson = personRepository.findByEmail(userModel.getEmail());
                if (persistedPerson.isPresent()) {
                    return HttpResponseModel.success("User already exists");
                }
                PersonModel newPerson = new PersonModel();
                newPerson.setEmail(userModel.getEmail());
                newPerson.setFirstName(userModel.getFirstName());
                newPerson.setLastName(userModel.getLastName());

                PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                newPerson.setPassword(encoder.encode(userModel.getPassword()));

                newPerson.setAccountStatus(PersonModel.AccountStatus.PENDING);

                personRepository.save(newPerson);
                return HttpResponseModel.success("User created");
            }

            return HttpResponseModel.error("Invalid user data");

        } catch (Exception err) {
            log.error(err.getMessage());
            return HttpResponseModel.error("Error registering new person");
        }
    }

    public Optional<PersonModel> getActivePersonByEmail(String email) {
        try {
            return personRepository.findByEmail(email);

        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to get person by id: " + e.getMessage());
        }
    }
}
