package pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class User implements Serializable {
	private String email;
	private String nickname;
	private String name;
	private String surname;
	@ToString.Exclude
	private String password;
	private LocalDate birthday;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private byte[] profilePicture;

//	@ToString.Exclude
//	@EqualsAndHashCode.Exclude
//	private List<Post> posts;
//
//	@ToString.Exclude
//	@EqualsAndHashCode.Exclude
//	private List<Comment> comments;

	public User(String email, String nickname, String name, String surname, String password, LocalDate birthday) {
		this.email = email;
		this.nickname = nickname;
		this.name = name;
		this.surname = surname;
		this.birthday = birthday;

		setPassword(password);
	}

	public void setPassword(String password) {
		this.password = DigestUtils.sha256Hex(password);
	}
}
