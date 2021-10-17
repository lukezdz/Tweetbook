package pl.edu.pg.zdziarski.lukasz.tweetbook.user.dto;

import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
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
public class CreateUserRequest {
    private String email;
    private String nickname;
    private String name;
    private String surname;
    private String password;
    private LocalDate birthday;

    public static Function<CreateUserRequest, User> dtoToEntityMapper() {
        return request -> User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .name(request.getName())
                .surname(request.getSurname())
                .password(DigestUtils.sha256Hex(request.getPassword()))
                .birthday(request.getBirthday())
                .build();
    };
}
