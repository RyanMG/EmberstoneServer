package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final PersonService personService;

    public TokenService(JwtEncoder jwtEncoder, PersonService personService) {
        this.jwtEncoder = jwtEncoder;
        this.personService = personService;
    }

    public HttpResponseModel<String> generateToken(PersonModel userDetails) {
        Optional<PersonModel> persistedUser = personService.getActivePersonByEmail(userDetails.getEmail());
        if (persistedUser.isPresent()) {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            boolean validPassword = encoder.matches(userDetails.getPassword(), persistedUser.get().getPassword());
            if (validPassword) {
                Instant now = Instant.now();

                JwtClaimsSet claims = JwtClaimsSet.builder()
                        .issuer("self")
                        .issuedAt(now)
                        .expiresAt(now.plus(1, ChronoUnit.HOURS))
                        .subject(userDetails.getEmail())
                        .build();

                String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
                return HttpResponseModel.success(token);
            }
        }
        return HttpResponseModel.error("Invalid username or password");
    }
}
