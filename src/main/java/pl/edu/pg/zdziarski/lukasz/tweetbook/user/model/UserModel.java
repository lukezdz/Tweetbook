package pl.edu.pg.zdziarski.lukasz.tweetbook.user.model;


import lombok.*;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import java.util.function.Function;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserModel {
    private String email;
    private String nickname;

    public static Function<User, UserModel> entityToModelMapper() {
        return user -> UserModel.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .build();
    }
}
