package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.repository.SeedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("seed")
public class SeedController {
    private final SeedRepository seedRepository;

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    public SeedController(SeedRepository seedRepository) {
        this.seedRepository = seedRepository;
    }

    @GetMapping("")
    public HttpResponseModel<String> seedDatabase() {
        try {
            log.info("Seed database starting...");
            seedRepository.seedDatabase();
            return HttpResponseModel.success("Database successfully loaded", null);

        } catch (Exception e) {
            return HttpResponseModel.error("Error seeding database: " + e.getMessage());
        }
    }
}
