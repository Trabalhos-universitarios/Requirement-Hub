package com.br.requirementhub.services;

import com.br.requirementhub.dtos.stakeholder.StakeholderResponseDto;
import com.br.requirementhub.entity.Stakeholder;
import com.br.requirementhub.repository.StakeHolderRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StakeholderService {

    @Autowired
    private StakeHolderRepository stakeholder;

    public Set<StakeholderResponseDto> getAllStakeholders() {
        List<Stakeholder> stakeholders = stakeholder.findAll();
        return stakeholders.stream()
                .map(this::toModel)
                .collect(Collectors.toSet());
    }

    private StakeholderResponseDto toModel(Stakeholder request) {
        StakeholderResponseDto dto = new StakeholderResponseDto();

        dto.setName(request.getName());

        return dto;
    }
}
