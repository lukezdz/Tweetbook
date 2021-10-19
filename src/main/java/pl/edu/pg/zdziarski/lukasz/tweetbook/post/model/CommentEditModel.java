package pl.edu.pg.zdziarski.lukasz.tweetbook.post.model;

import lombok.*;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Comment;

import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CommentEditModel {
    private String id;
    private String description;

    public static Function<Comment, CommentEditModel> entityToModelMapper() {
        return comment -> CommentEditModel.builder()
                .id(comment.getId())
                .description(comment.getDescription())
                .build();
    }

    public static BiFunction<Comment, CommentEditModel, Comment> modelToEntityMapper() {
        return (comment, model) -> {
            comment.setDescription(model.getDescription());
            return comment;
        };
    }
}
