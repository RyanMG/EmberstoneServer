package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.service.RosterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/roster")
public class RosterController {
    private final RosterService rosterService;
    public RosterController(RosterService rosterService) {
        this.rosterService = rosterService;
    }

}
