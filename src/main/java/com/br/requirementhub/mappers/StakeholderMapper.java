package com.br.requirementhub.mappers;

import com.br.requirementhub.dtos.stakeholder.StakeholderResponseDto;
import com.br.requirementhub.entity.Stakeholder;

public interface StakeholderMapper {

    static StakeholderResponseDto toModel(Stakeholder request) {
        return StakeholderResponseDto.builder()
                .name(request.getName())
                .build();
    }
}
