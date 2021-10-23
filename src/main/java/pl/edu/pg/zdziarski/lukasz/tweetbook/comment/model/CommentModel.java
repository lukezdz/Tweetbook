package pl.edu.pg.zdziarski.lukasz.tweetbook.comment.model;

import lombok.*;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.entity.Comment;

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CommentModel {
    private String id;
    private String authorsEmail;
    private String target;
    private String description;
    private LocalDateTime creationTime;

    public static Function<Comment, CommentModel> entityToModelMapper() {
        return comment -> CommentModel.builder()
                .id(comment.getId())
                .authorsEmail(comment.getAuthor().getEmail())
                .target(comment.getTarget().getId())
                .description(comment.getDescription())
                .creationTime(comment.getCreationTime())
                .build();
    }
}
