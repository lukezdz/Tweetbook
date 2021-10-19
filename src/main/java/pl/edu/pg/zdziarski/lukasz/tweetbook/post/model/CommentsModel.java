package pl.edu.pg.zdziarski.lukasz.tweetbook.post.model;

import lombok.*;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Comment;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CommentsModel {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class Comment {
        private String id;
        private String authorsEmail;
        private String target;
        private String description;
        private LocalDateTime creationTime;
    }

    @Singular
    private List<Comment> comments;

    public static Function<Collection<pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Comment>, CommentsModel> entityToModelMapper() {
        return comments -> {
            CommentsModel.CommentsModelBuilder builder = CommentsModel.builder();
            comments.stream()
                    .map(comment -> Comment.builder()
                            .id(comment.getId())
                            .authorsEmail(comment.getAuthor().getEmail())
                            .target(comment.getTarget().getId())
                            .description(comment.getDescription())
                            .creationTime(comment.getCreationTime())
                            .build())
                    .forEach(builder::comment);
            return builder.build();
        };
    }
}
