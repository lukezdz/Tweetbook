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

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Supplier;

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
	private LocalDate creationTime;

	public static Function<CreatePostRequest, Post> dtoToEntityMapper(Supplier<User> userSupplier) {
		return request -> Post.builder()
				.id(request.getId())
				.description(request.getDescription())
				.creationTime(request.getCreationTime())
				.author(userSupplier.get())
				.build();
	}
}
