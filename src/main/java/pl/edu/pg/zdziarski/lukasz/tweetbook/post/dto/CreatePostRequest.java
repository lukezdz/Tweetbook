package pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
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
public class CreatePostRequest {
	private String description;
	private String authorsEmail;

	public static Function<CreatePostRequest, Post> dtoToEntityMapper(UserService userService) {
		return request -> {
			Optional<User> user = userService.find(request.getAuthorsEmail());
			if (user.isPresent()) {
				return new Post(user.get(), request.getDescription(), LocalDateTime.now());
			}

			throw new IllegalArgumentException();
		};
	}
}
