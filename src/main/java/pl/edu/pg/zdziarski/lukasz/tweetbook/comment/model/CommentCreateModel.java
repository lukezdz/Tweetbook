package pl.edu.pg.zdziarski.lukasz.tweetbook.comment.model;

import lombok.*;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.PostService;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CommentCreateModel {
    private String description;
    private String authorsEmail;
    private String postId;

    public static Function<CommentCreateModel, Comment> modelToEntityMapper(UserService userService, PostService postService) {
        return model -> {
            Optional<User> user = userService.find(model.getAuthorsEmail());
            Optional<Post> post = postService.find(model.getPostId());

            if (user.isPresent() && post.isPresent()) {
                return new Comment(user.get(), post.get(), LocalDateTime.now(), model.getDescription());
            }
            else {
                throw new IllegalArgumentException("No user with given email or post with given id");
            }
        };
    }
}
