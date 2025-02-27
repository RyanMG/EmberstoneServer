package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.FactionModel;
import com.emberstone.emberstone_tavern.model.GrandAllianceModel;
import com.emberstone.emberstone_tavern.service.SigmarDataService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/sigmar")
public class SigmarDataController {
    private final SigmarDataService sigmarDataService;

    public SigmarDataController(SigmarDataService sigmarDataService) {
        this.sigmarDataService = sigmarDataService;
    }

    @GetMapping("/grand-alliances")
    public List<GrandAllianceModel> getGrandAlliances() {
        return sigmarDataService.getGrandAlliances();
    }

    @GetMapping("/factions")
    public List<FactionModel> getFactions(@RequestParam(required = false, name = "grandAllianceId") Integer grandAllianceId) {
        return sigmarDataService.getFactions(grandAllianceId);
    }
}
