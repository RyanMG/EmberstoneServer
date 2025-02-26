package com.emberstone.emberstone_tavern.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class MemberDTO {
    private UUID id;

    private String firstName;

    private String lastName;
}
