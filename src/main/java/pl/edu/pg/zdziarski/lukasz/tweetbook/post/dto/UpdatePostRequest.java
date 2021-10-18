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

import java.util.function.BiFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdatePostRequest {
	private String description;

	public static BiFunction<Post, UpdatePostRequest, Post> dtoToEntityMapper() {
		return (post, request) -> {
			post.setDescription(request.getDescription());
			return post;
		};
	}
}
