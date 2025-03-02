package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.controllers.AuthController;
import com.emberstone.emberstone_tavern.dto.MemberDTO;
import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.dto.UserDTO;
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

    private Boolean validatePersonData(UserDTO userModel) {
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
    public HttpResponseModel<String> registerNewPerson(UserDTO userModel) {
        try {
            Boolean isValidUser = validatePersonData(userModel);

            if (isValidUser) {
                Optional<PersonModel> persistedPerson = personRepository.findByEmail(userModel.getEmail());
                if (persistedPerson.isPresent()) {
                    return HttpResponseModel.success("User already exists", null);
                }
                PersonModel newPerson = new PersonModel();
                newPerson.setEmail(userModel.getEmail());
                newPerson.setFirstName(userModel.getFirstName());
                newPerson.setLastName(userModel.getLastName());

                PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                newPerson.setPassword(encoder.encode(userModel.getPassword()));

                newPerson.setAccountStatus(PersonModel.AccountStatus.PENDING);

                personRepository.save(newPerson);
                return HttpResponseModel.success("User created", null);
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
            throw new RuntimeException("Failed to get active person by email: " + e.getMessage());
        }
    }

    public Optional<MemberDTO> getPersonByEmail(String email) {
        try {
            Optional<PersonModel> personModel = personRepository.findByEmail(email);
            if (personModel.isPresent()) {
                MemberDTO person = new MemberDTO();
                person.setId(personModel.get().getId());
                person.setFirstName(personModel.get().getFirstName());
                person.setLastName(personModel.get().getLastName());
                return Optional.of(person);
            }

            return Optional.empty();

        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to get person by email: " + e.getMessage());
        }
    }

    public Optional<PersonModel> getPersonById(UUID id) {
        try {
            return personRepository.findById(id);

        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to get person by id: " + e.getMessage());
        }
    }
}
