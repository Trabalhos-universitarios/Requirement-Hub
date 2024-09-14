package com.br.requirementhub.controller;

import com.br.requirementhub.entity.RequirementHistory;
import com.br.requirementhub.services.RequirementHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requirement-history")
public class RequirementHistoryController {

    @Autowired
    private com.br.requirementhub.services.RequirementHistoryService requirementHistoryService;

    @GetMapping("/{projectId}")
    public List<RequirementHistory> getHistoryByProject(@PathVariable Long projectId) {
        return requirementHistoryService.getHistoryByProject(projectId);
    }

    @GetMapping("/{identifier}/{projectId}")
    public List<RequirementHistory> getHistoryByIdentifierAndProject(@PathVariable String identifier, @PathVariable Long projectId) {
        return requirementHistoryService.getHistoryByIdentifierAndProject(identifier, projectId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        requirementHistoryService.deleteHistory(id);
        return ResponseEntity.ok().build();
    }
}