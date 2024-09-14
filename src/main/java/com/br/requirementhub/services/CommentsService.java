package com.br.requirementhub.services;

import com.br.requirementhub.dtos.comments.CommentsRequestDto;
import com.br.requirementhub.dtos.comments.CommentsCreateResponseDto;
import com.br.requirementhub.entity.Comments;
import com.br.requirementhub.entity.User;
import com.br.requirementhub.repository.CommentsRepository;
import com.br.requirementhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;

    private final UserRepository userRepository;

    public CommentsCreateResponseDto saveComment(CommentsRequestDto commentDto) {
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

    private CommentsCreateResponseDto convertToResponseDTO(Comments comments) {

        final User user = userRepository.findById(comments.getUser().getId()).orElse(null);

        CommentsCreateResponseDto dto = new CommentsCreateResponseDto();
        dto.setDescription(comments.getDescription());
        dto.setRequirementId(comments.getRequirement().getId());
        dto.setUserName(comments.getUser().getName());
        dto.setUserImage(comments.getAvatarUser());
        assert user != null;
        dto.setUserRole(user.getRole().name());
        dto.setDateCreated(comments.getDateCreated().toString());
        dto.setReactions(comments.getReactions());
        return dto;
    }
}

