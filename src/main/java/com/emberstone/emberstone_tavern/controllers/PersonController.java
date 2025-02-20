package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{id}")
    public Optional<PersonModel> getActivePersonById(@PathVariable UUID id) {
        return personService.getActivePersonById(id);
    }
}
