package com.br.requirementhub.dtos.comments;

import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentsRequestDto {

    private String description;
    private Requirement requirementId;
    private User user;
    private String avatarUser;
    private List<String> reactions;
}
