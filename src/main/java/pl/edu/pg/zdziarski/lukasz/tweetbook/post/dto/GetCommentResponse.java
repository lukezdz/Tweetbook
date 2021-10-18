package pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Comment;

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetCommentResponse {
	private String id;
	private String postId;
	private String userEmail;
	private String description;
	private LocalDateTime creationTime;

	public static Function<Comment, GetCommentResponse> entityToDtoMapper() {
		return comment -> GetCommentResponse.builder()
				.id(comment.getId())
				.postId(comment.getTarget().getId())
				.userEmail(comment.getAuthor().getEmail())
				.description(comment.getDescription())
				.creationTime(comment.getCreationTime())
				.build();
	}
}
