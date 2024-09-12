package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.comments.CommentsRequestDto;
import com.br.requirementhub.dtos.comments.CommentsResponseDto;
import com.br.requirementhub.entity.Comments;
import com.br.requirementhub.services.CommentsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentsController {

    private CommentsService commentsService;

    @PostMapping
    public ResponseEntity<CommentsResponseDto> addComment(@RequestBody CommentsRequestDto comment) {

        CommentsResponseDto commentResponse = commentsService.saveComment(comment);

        return ResponseEntity.status(201).body(commentResponse);
    }

    @GetMapping("/{requirementId}")
    public List<Comments> getCommentsByRequirement(@PathVariable Long requirementId) {
        return commentsService.getAllCommentsForRequirement(requirementId);
    }
}

