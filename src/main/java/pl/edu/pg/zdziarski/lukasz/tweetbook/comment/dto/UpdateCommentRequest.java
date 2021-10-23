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

import java.util.function.BiFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateCommentRequest {
	private String description;

	public static BiFunction<Comment, UpdateCommentRequest, Comment> dtoToEntityMapper() {
		return (comment, request) -> {
			comment.setDescription(request.getDescription());
			return comment;
		};
	}
}
