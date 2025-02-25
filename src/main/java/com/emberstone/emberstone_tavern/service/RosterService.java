package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.repository.RosterRepository;
import org.springframework.stereotype.Service;

@Service
public class RosterService {
    private final RosterRepository rosterRepository;

    public RosterService(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

}
