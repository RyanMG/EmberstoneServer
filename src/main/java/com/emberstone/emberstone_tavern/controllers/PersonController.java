package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.PersonModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import com.emberstone.emberstone_tavern.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/person")
public class PersonController {
    private final PersonService personService;

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("")
    public Optional<PersonModel> getActivePersonById(Authentication authentication) {
        return personService.getActivePersonByEmail(authentication.getName());
    }

    @GetMapping("{id}")
    public Optional<PersonModel> getPersonById(@PathVariable UUID id) {
        return personService.getPersonById(id);
    }
}
