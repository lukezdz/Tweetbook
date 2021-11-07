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
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Entity
@Table(name = "users")
public class User implements Serializable {
	@Id
	private String email;
	private String nickname;
	private String name;
	private String surname;
	@ToString.Exclude
	private String password;
	private LocalDate birthday;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private byte[] profilePicture;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
	private List<Post> posts;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
	private List<Comment> comments;

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
