package com.br.requirementhub.dtos.stakeholder;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class StakeholderRequestDto {

    private String name;
    private Long requirementId;
}
