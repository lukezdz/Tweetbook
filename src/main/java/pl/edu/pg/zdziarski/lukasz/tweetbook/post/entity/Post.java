package pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity;

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
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "posts")
public class Post implements Serializable {
	@Id
	private String id;

	@ManyToOne
	@JoinColumn(name = "users")
	private User author;
	private String description;
	private LocalDateTime creationTime;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "target", cascade = CascadeType.REMOVE)
	private List<Comment> comments;

	public Post(User author, String description, LocalDateTime creationTime) {
		this.id = DigestUtils.sha256Hex((author.getEmail() + description + creationTime.toString())
				.getBytes(StandardCharsets.UTF_8));
		this.author = author;
		this.description = description;
		this.creationTime = creationTime;
	}
}
