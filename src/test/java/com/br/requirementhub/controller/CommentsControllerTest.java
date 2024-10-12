package com.br.requirementhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.br.requirementhub.dtos.comments.CommentsCreateResponseDto;
import com.br.requirementhub.dtos.comments.CommentsReactRequestDto;
import com.br.requirementhub.dtos.comments.CommentsRequestDto;
import com.br.requirementhub.services.CommentsService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class CommentsControllerTest {

    @Mock
    private CommentsService commentsService;

    @InjectMocks
    private CommentsController commentsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCommentSuccessfully() {
        CommentsRequestDto requestDto = new CommentsRequestDto();
        CommentsCreateResponseDto responseDto = new CommentsCreateResponseDto();
        when(commentsService.saveComment(any(CommentsRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<CommentsCreateResponseDto> result = commentsController.addComment(requestDto);

        assertEquals(ResponseEntity.status(201).body(responseDto), result);
        verify(commentsService, times(1)).saveComment(requestDto);
    }

    @Test
    void getCommentsByRequirementSuccessfully() {
        Long requirementId = 1L;
        List<CommentsCreateResponseDto> responseList = Collections.singletonList(new CommentsCreateResponseDto());
        when(commentsService.getAllCommentsByRequirement(requirementId)).thenReturn(responseList);

        List<CommentsCreateResponseDto> result = commentsController.getCommentsByRequirement(requirementId);

        assertEquals(responseList, result);
        verify(commentsService, times(1)).getAllCommentsByRequirement(requirementId);
    }

    @Test
    void getAllCommentsSuccessfully() {
        List<CommentsCreateResponseDto> responseList = Collections.singletonList(new CommentsCreateResponseDto());
        when(commentsService.getAllComments()).thenReturn(responseList);

        List<CommentsCreateResponseDto> result = commentsController.getAllComments();

        assertEquals(responseList, result);
        verify(commentsService, times(1)).getAllComments();
    }

    @Test
    void addReactSuccessfully() {
        Long commentId = 1L;
        CommentsReactRequestDto requestDto = new CommentsReactRequestDto();
        CommentsCreateResponseDto responseDto = new CommentsCreateResponseDto();
        when(commentsService.addReact(anyLong(), any(CommentsReactRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<CommentsCreateResponseDto> result = commentsController.addReact(commentId, requestDto);

        assertEquals(ResponseEntity.ok(responseDto), result);
        verify(commentsService, times(1)).addReact(commentId, requestDto);
    }

    @Test
    void deleteCommentSuccessfully() {
        Long commentId = 1L;
        doNothing().when(commentsService).deleteComment(commentId);

        ResponseEntity<CommentsCreateResponseDto> result = commentsController.deleteComment(commentId);

        assertEquals(ResponseEntity.noContent().build(), result);
        verify(commentsService, times(1)).deleteComment(commentId);
    }
}
