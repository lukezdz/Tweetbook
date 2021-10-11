package pl.edu.pg.zdziarski.lukasz.tweetbook.user.dto;

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
public class GetUsersResponse {

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@ToString
	@EqualsAndHashCode
	public static class User {
		private String email;
		private String nickname;
	}

	@Singular
	private List<User> users;

	public static Function<Collection<pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User>, GetUsersResponse> entityToDtoMapper() {
		return users -> {
			GetUsersResponse.GetUsersResponseBuilder response = GetUsersResponse.builder();
			users.stream()
					.map(user -> User.builder()
							.email(user.getEmail())
							.nickname(user.getNickname())
							.build())
					.forEach(response::user);
			return response.build();
		};
	}
}
