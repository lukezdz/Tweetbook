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
	private String id;
	private String description;
	private String userEmail;
	private LocalDateTime creationTime;

	public static Function<CreatePostRequest, Post> dtoToEntityMapper(UserService userService) {
		return request -> {
			Optional<User> user = userService.find(request.getUserEmail());
			if (user.isPresent()) {
				return Post.builder()
						.id(request.getId())
						.description(request.getDescription())
						.creationTime(request.getCreationTime())
						.author(user.get())
						.build();
			}

			throw new IllegalArgumentException();
		};
	}
}
