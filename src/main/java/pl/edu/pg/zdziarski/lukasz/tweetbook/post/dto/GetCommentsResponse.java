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
public class GetCommentsResponse {

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@ToString
	@EqualsAndHashCode
	public static class Comment {
		private String id;
	}

	@Singular
	private List<Comment> comments;

	public static Function<Collection<pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Comment>, GetCommentsResponse> entityToDtoMapper() {
		return comments -> {
			GetCommentsResponse.GetCommentsResponseBuilder response = GetCommentsResponse.builder();
			comments.stream()
					.map(comment -> Comment.builder()
							.id(comment.getId())
							.build())
					.forEach(response::comment);
			return response.build();
		};
	}
}
