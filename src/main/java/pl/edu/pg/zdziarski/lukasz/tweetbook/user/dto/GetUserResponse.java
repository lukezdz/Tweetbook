package pl.edu.pg.zdziarski.lukasz.tweetbook.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import java.time.LocalDate;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetUserResponse {
	private String email;
	private String nickname;
	private String name;
	private String surname;
	private LocalDate birthday;

	public static Function<User, GetUserResponse> entityToDtoMapper() {
		return user -> GetUserResponse.builder()
				.email(user.getEmail())
				.nickname(user.getNickname())
				.name(user.getName())
				.surname(user.getSurname())
				.birthday(user.getBirthday())
				.build();
	}
}
