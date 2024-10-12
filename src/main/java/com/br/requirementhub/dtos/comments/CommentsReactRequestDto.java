package com.br.requirementhub.dtos.comments;

import com.br.requirementhub.entity.CommentReaction;
import com.br.requirementhub.entity.Requirement;
import com.br.requirementhub.entity.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsReactRequestDto {

    private User user;
    private CommentReaction reactions;
}
