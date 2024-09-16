package com.br.requirementhub.dtos.comments;

import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class CommentsCreateResponseDto {

    private Long id;
    private String description;
    private Long requirementId;
    private Long userId;
    private String userName;
    private String userRole;
    private String dateCreated;
    private List<String> reactions;
    private String userImage;
}
