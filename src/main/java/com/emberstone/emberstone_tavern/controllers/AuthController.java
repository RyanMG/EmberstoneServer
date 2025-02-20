package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.model.UserModel;
import com.emberstone.emberstone_tavern.service.PersonService;
import com.emberstone.emberstone_tavern.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;
    private final PersonService personService;

    public AuthController(TokenService tokenService, PersonService personService) {
        this.tokenService = tokenService;
        this.personService = personService;
    }

    @PostMapping("/register")
    public HttpResponseModel<String> getActivePersonByEmail(@RequestBody UserModel userDetails) {
        return personService.registerNewPerson(userDetails);
    }

    @PostMapping("/login")
    public HttpResponseModel<String> login(@RequestBody UserModel userDetails) {
        return tokenService.generateToken(userDetails);
    }
}
