package com.br.requirementhub.controller;

import com.br.requirementhub.dtos.comments.CommentsReactRequestDto;
import com.br.requirementhub.dtos.comments.CommentsRequestDto;
import com.br.requirementhub.dtos.comments.CommentsCreateResponseDto;
import com.br.requirementhub.services.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments", produces = "application/json")
public class CommentsController {

    private final CommentsService commentsService;

    @Operation(summary = "Create a new comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Comment already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CommentsCreateResponseDto> addComment(@RequestBody CommentsRequestDto comment) {

        CommentsCreateResponseDto commentResponse = commentsService.saveComment(comment);

        return ResponseEntity.status(201).body(commentResponse);
    }

    @Operation(summary = "Get all comments by requirement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{requirementId}")
    public List<CommentsCreateResponseDto> getCommentsByRequirement(@PathVariable Long requirementId) {
        return commentsService.getAllCommentsByRequirement(requirementId);
    }

    @Operation(summary = "Get all comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<CommentsCreateResponseDto> getAllComments() {
        return commentsService.getAllComments();
    }

    @Operation(summary = "Add a reaction to a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reaction added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Reaction already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CommentsCreateResponseDto> addReact(
            @PathVariable Long id,
            @RequestBody CommentsReactRequestDto requestDto) {

        CommentsCreateResponseDto response = commentsService.addReact(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a comment")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommentsCreateResponseDto> deleteComment(@PathVariable Long id) {
        commentsService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

}
