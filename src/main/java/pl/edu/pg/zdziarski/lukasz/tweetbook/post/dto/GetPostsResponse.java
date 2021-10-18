package pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetPostsResponse {

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@ToString
	@EqualsAndHashCode
	public static class Post {
		private String id;
	}

	@Singular
	private List<Post> posts;

	public static Function<Collection<pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post>, GetPostsResponse> entityToDtoMapper() {
		return posts -> {
			GetPostsResponse.GetPostsResponseBuilder response = GetPostsResponse.builder();
			posts.stream()
					.map(post -> Post.builder()
							.id(post.getId())
							.build())
					.forEach(response::post);
			return response.build();
		};
	}
}
