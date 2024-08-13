package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.stakeholder.StakeholderResponseDto;
import com.br.requirementhub.services.StakeholderService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stakeholders")
public class StakeholderController {

    private final StakeholderService service;

    @GetMapping
    public ResponseEntity<Set<StakeholderResponseDto>> getAllStakeholders() {
        return ResponseEntity.ok(service.getAllStakeholders());
    }
}
