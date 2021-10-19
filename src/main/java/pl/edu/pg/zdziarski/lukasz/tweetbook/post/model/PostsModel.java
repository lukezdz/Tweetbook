package pl.edu.pg.zdziarski.lukasz.tweetbook.post.model;

import lombok.*;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;

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
public class PostsModel {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class Post {
        private String id;
        private String authorsEmail;
        private String description;
        private LocalDateTime creationTime;
    }

    @Singular
    private List<Post> posts;

    public static Function<Collection<pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post>, PostsModel> entityToModelMapper() {
        return posts -> {
            PostsModel.PostsModelBuilder builder = PostsModel.builder();
            posts.stream()
                    .map(post -> Post.builder()
                            .id(post.getId())
                            .authorsEmail(post.getAuthor().getEmail())
                            .description(post.getDescription())
                            .creationTime(post.getCreationTime())
                            .build())
                    .forEach(builder::post);
            return builder.build();
        };
    }
}
