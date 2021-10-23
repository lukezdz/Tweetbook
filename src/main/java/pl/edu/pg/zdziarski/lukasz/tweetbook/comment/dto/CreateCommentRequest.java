package pl.edu.pg.zdziarski.lukasz.tweetbook.comment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateCommentRequest {
	private String authorEmail;
	private String postId;
	private String description;

	public static Function<CreateCommentRequest, Comment> dtoToEntityMapper(UserService userService, PostService postService) {
		return request -> {
			Optional<User> user = userService.find(request.getAuthorEmail());
			Optional<Post> post = postService.find(request.getPostId());

			if (user.isPresent() && post.isPresent()) {
				return new Comment(user.get(), post.get(), LocalDateTime.now(), request.getDescription());
			}
			else if (user.isEmpty()) {
				throw new IllegalArgumentException("There is no user with such email");
			}
			throw new IllegalArgumentException("There is no post with such id");
		};
	}
}
