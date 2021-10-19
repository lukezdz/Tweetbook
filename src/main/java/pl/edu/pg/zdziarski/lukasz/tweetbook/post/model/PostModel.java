package pl.edu.pg.zdziarski.lukasz.tweetbook.post.model;

import lombok.*;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PostModel {
    private String id;
    private String authorsEmail;
    private String description;
    private LocalDateTime creationTime;

    public static Function<Post, PostModel> entityToModelMapper() {
        return post -> PostModel.builder()
                .id(post.getId())
                .authorsEmail(post.getAuthor().getEmail())
                .description(post.getDescription())
                .creationTime(post.getCreationTime())
                .build();
    }
}
