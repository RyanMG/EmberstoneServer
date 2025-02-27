package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.FactionModel;
import com.emberstone.emberstone_tavern.model.GrandAllianceModel;
import com.emberstone.emberstone_tavern.repository.FactionRepository;
import com.emberstone.emberstone_tavern.repository.GrandAllianceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SigmarDataService {
    private final GrandAllianceRepository grandAllianceRepository;
    private final FactionRepository factionRepository;

    public SigmarDataService(GrandAllianceRepository grandAllianceRepository, FactionRepository factionRepository) {
        this.grandAllianceRepository = grandAllianceRepository;
        this.factionRepository = factionRepository;
    }

    public List<GrandAllianceModel> getGrandAlliances() {
        try {
            return grandAllianceRepository.findAll();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get grand alliances: " + e.getMessage());
        }
    }

    public List<FactionModel> getFactions(Integer grandAllianceId) {
        try {
            if (grandAllianceId == null) {
                return factionRepository.findAll();
            }

            return factionRepository.getFactionsByGrandAlliance(grandAllianceId);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get factions: " + e.getMessage());
        }
    }
}
