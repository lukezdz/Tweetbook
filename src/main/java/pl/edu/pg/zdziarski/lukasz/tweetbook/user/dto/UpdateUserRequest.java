package pl.edu.pg.zdziarski.lukasz.tweetbook.user.dto;

import lombok.*;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import java.time.LocalDate;
import java.util.function.BiFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateUserRequest {
    private String nickname;
    private String name;
    private String surname;
    private LocalDate birthday;

    public static BiFunction<User, UpdateUserRequest, User> dtoToEntityMapper() {
        return (user, request) -> {
            user.setNickname(request.getNickname());
            user.setName(request.getName());
            user.setSurname(request.getSurname());
            user.setBirthday(request.getBirthday());
            return user;
        };
    }
}
