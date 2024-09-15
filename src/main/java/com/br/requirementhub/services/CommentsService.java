package com.br.requirementhub.services;

import com.br.requirementhub.dtos.comments.CommentsCreateResponseDto;
import com.br.requirementhub.dtos.comments.CommentsReactRequestDto;
import com.br.requirementhub.dtos.comments.CommentsRequestDto;
import com.br.requirementhub.entity.CommentReaction;
import com.br.requirementhub.entity.Comments;
import com.br.requirementhub.entity.User;
import com.br.requirementhub.exceptions.NotFoundException;
import com.br.requirementhub.repository.CommentReactionRepository;
import com.br.requirementhub.repository.CommentsRepository;
import com.br.requirementhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;

    private final UserRepository userRepository;

    private final CommentReactionRepository commentReactionRepository;

    public CommentsCreateResponseDto saveComment(CommentsRequestDto commentDto) {
        Comments comments = convertToEntity(commentDto);
        comments = commentsRepository.save(comments);
        return convertToResponseDTO(comments);
    }

    public List<CommentsCreateResponseDto> getAllCommentsForRequirement(Long requirementId) {
        List<Comments> comments = commentsRepository.findByRequirementId(requirementId);

        return comments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CommentsCreateResponseDto> getAllComments() {
        List<Comments> comments = commentsRepository.findAll();

        return comments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public CommentsCreateResponseDto addReact(Long id, CommentsReactRequestDto comment) {
        if (verifyCommentAlreadyExistByUser(id, comment)) {
            Comments comments = commentsRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Comment not found!"));
            return convertToResponseDTO(comments);
        }

        Comments comments = commentsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found!"));

        CommentReaction newReaction = new CommentReaction();
        newReaction.setComment(comments);
        newReaction.setUser(comment.getUser());
        newReaction.setReaction(comment.getReactions().getReaction());

        commentReactionRepository.save(newReaction);

        comments.getReactions().add(newReaction);

        return convertToResponseDTO(comments);
    }

    private boolean verifyCommentAlreadyExistByUser(Long id, CommentsReactRequestDto comment) {
        Comments comments = commentsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found!"));
        CommentReaction existingReaction = commentReactionRepository.findByCommentAndUser(comments, comment.getUser());

        if (existingReaction != null) {
            existingReaction.setReaction(comment.getReactions().getReaction());
            commentReactionRepository.save(existingReaction);
            return true;
        }

        return false;
    }

    private Comments convertToEntity(CommentsRequestDto dto) {
        Comments comments = new Comments();
        comments.setDescription(dto.getDescription());
        comments.setRequirement(dto.getRequirement());
        comments.setUser(dto.getUser());
        comments.setAvatarUser(dto.getAvatarUser());
        comments.setReactions(dto.getReactions());
        return comments;
    }

    private CommentsCreateResponseDto convertToResponseDTO(Comments comments) {

        final User user = userRepository.findById(comments.getUser().getId()).orElse(null);

        CommentsCreateResponseDto dto = new CommentsCreateResponseDto();
        dto.setId(comments.getId());
        dto.setDescription(comments.getDescription());
        dto.setRequirementId(comments.getRequirement().getId());
        dto.setUserId(comments.getUser().getId());
        dto.setUserName(comments.getUser().getName());
        dto.setUserImage(comments.getAvatarUser());
        assert user != null;
        dto.setUserRole(user.getRole().name());
        dto.setDateCreated(comments.getDateCreated().toString());
        dto.setReactions(comments.getReactions()
                .stream()
                .map(CommentReaction::getReaction)
                .collect(Collectors.toList()));
        return dto;
    }
}

