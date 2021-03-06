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

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetPostResponse {
	private String id;
	private String description;
	private String authorsEmail;
	private LocalDateTime creationTime;

	public static Function<Post, GetPostResponse> entityToDtoMapper() {
		return post -> GetPostResponse.builder()
				.id(post.getId())
				.description(post.getDescription())
				.authorsEmail(post.getAuthor().getEmail())
				.creationTime(post.getCreationTime())
				.build();
	}
}
