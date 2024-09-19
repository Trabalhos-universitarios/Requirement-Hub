package com.br.requirementhub.repository;

import com.br.requirementhub.dtos.comments.CommentsCreateResponseDto;
import com.br.requirementhub.dtos.comments.CommentsReactResponseDto;
import com.br.requirementhub.entity.CommentReaction;
import com.br.requirementhub.entity.Comments;
import com.br.requirementhub.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
    CommentReaction findByCommentAndUser(Comments comment, User user);

    List<CommentReaction> findByCommentAndUserOrderById(Comments comment, User user);

    @Query("SELECT new com.br.requirementhub.dtos.comments.CommentsReactResponseDto(cr.id, cr.comment.id, CAST(cr.user.id AS string), cr.reaction) " +
            "FROM CommentReaction cr WHERE cr.comment = :comment AND cr.user = :user")
    List<CommentsReactResponseDto> findByCommentAndUserResponse(@Param("comment") Comments comment, @Param("user") User user);

    List<CommentReaction> findByComment(Comments comment);

}
