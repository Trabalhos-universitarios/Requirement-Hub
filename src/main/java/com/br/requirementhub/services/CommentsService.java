package com.br.requirementhub.services;

import com.br.requirementhub.dtos.comments.CommentsCreateResponseDto;
import com.br.requirementhub.dtos.comments.CommentsReactRequestDto;
import com.br.requirementhub.dtos.comments.CommentsReactResponseDto;
import com.br.requirementhub.dtos.comments.CommentsRequestDto;
import com.br.requirementhub.entity.CommentReaction;
import com.br.requirementhub.entity.Comments;
import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.User;
import com.br.requirementhub.exceptions.NotFoundException;
import com.br.requirementhub.exceptions.RequirementNotFoundException;
import com.br.requirementhub.repository.CommentReactionRepository;
import com.br.requirementhub.repository.CommentsRepository;
import com.br.requirementhub.repository.RequirementRepository;
import com.br.requirementhub.repository.UserRepository;
import com.br.requirementhub.enums.Status;
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

    private final RequirementService requirementService;

    private final RequirementRepository requirementRepository;

    public CommentsCreateResponseDto saveComment(CommentsRequestDto commentDto) {
        Requirement requirement = requirementRepository.findById(commentDto.getRequirement().getId())
                .orElseThrow(() -> new RequirementNotFoundException("Requirement not found: " + commentDto.getRequirement().getId()));
        Comments comments = convertToEntity(commentDto);
        comments = commentsRepository.save(comments);

        if(requirement.getStatus().equals(Status.ACTIVE.toString())){
            requirementService.createNotificationToUsers(requirement);
        }
        return convertToResponseDTO(comments);
    }

    public List<CommentsCreateResponseDto> getAllCommentsByRequirement(Long requirementId) {
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

    public void deleteComment(Long id) {
        commentsRepository.deleteById(id);
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

        final List<CommentReaction> reactions = commentReactionRepository.findByComment(comments);

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
        dto.setReactions(reactions.stream().map(reaction -> {
            CommentsReactResponseDto reactionDto = new CommentsReactResponseDto();
            reactionDto.setId(reaction.getId());
            reactionDto.setCommentId(reaction.getComment().getId());
            reactionDto.setUserId(reaction.getUser().getName());
            reactionDto.setEmoji(reaction.getReaction());
            return reactionDto;
        }).toList());
        return dto;
    }
}
