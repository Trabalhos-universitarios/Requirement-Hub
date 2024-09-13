package com.br.requirementhub.services;

import com.br.requirementhub.dtos.comments.CommentsRequestDto;
import com.br.requirementhub.dtos.comments.CommentsResponseDto;
import com.br.requirementhub.entity.Comments;
import com.br.requirementhub.repository.CommentsRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;

    public CommentsResponseDto saveComment(CommentsRequestDto commentDto) {
        Comments comments = convertToEntity(commentDto);
        comments = commentsRepository.save(comments);
        return convertToResponseDTO(comments);
    }

    public List<Comments> getAllCommentsForRequirement(Long requirementId) {
        return commentsRepository.findByRequirementId(requirementId);
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

    private CommentsResponseDto convertToResponseDTO(Comments requirement) {
        CommentsResponseDto dto = new CommentsResponseDto();
        dto.setDescription(requirement.getDescription());
        dto.setRequirementId(requirement.getRequirement());
        dto.setUser(requirement.getUser());
        dto.setAvatarUser(requirement.getAvatarUser());
        dto.setReactions(requirement.getReactions());
        return dto;
    }
}

