package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.comments.CommentsReactRequestDto;
import com.br.requirementhub.dtos.comments.CommentsRequestDto;
import com.br.requirementhub.dtos.comments.CommentsCreateResponseDto;
import com.br.requirementhub.services.CommentsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentsController {

    private final CommentsService commentsService;

    @PostMapping
    public ResponseEntity<CommentsCreateResponseDto> addComment(@RequestBody CommentsRequestDto comment) {

        CommentsCreateResponseDto commentResponse = commentsService.saveComment(comment);

        return ResponseEntity.status(201).body(commentResponse);
    }

    @GetMapping("/{requirementId}")
    public List<CommentsCreateResponseDto> getCommentsByRequirement(@PathVariable Long requirementId) {
        return commentsService.getAllCommentsForRequirement(requirementId);
    }

    @GetMapping
    public List<CommentsCreateResponseDto> getAllComments() {
        return commentsService.getAllComments();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommentsCreateResponseDto> addReact(
            @PathVariable Long id,
            @RequestBody CommentsReactRequestDto requestDto) {

        CommentsCreateResponseDto response = commentsService.addReact(id, requestDto);
        return ResponseEntity.ok(response);
    }

}

