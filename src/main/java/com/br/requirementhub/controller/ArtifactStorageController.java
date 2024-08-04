package com.br.requirementhub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artifact")
public class ArtifactStorageController {


//    @GetMapping("/download/{id}")
//    public ResponseEntity<Resource> downloadArtifact(@PathVariable Long id) {
//        ProjectResponseDTO project = service.findById(id);
//        if (project == null) {
//            return ResponseEntity.notFound().build();
//        }
//        byte[] artifact = project.getArtifact_file();
//        if (artifact != null && artifact.length > 0) {
//            ByteArrayResource resource = new ByteArrayResource(artifact);
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=artifact")
//                    .contentLength(artifact.length)
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(resource);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
