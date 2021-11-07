package pl.edu.pg.zdziarski.lukasz.tweetbook.comment.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
@Entity
@Table(name = "comments")
public class Comment implements Serializable {
	@Id
	private String id;

	@ManyToOne
	@JoinColumn(name = "users")
	private User author;

	@ManyToOne
	@JoinColumn(name = "posts")
	private Post target;

	private LocalDateTime creationTime;
	private String description;

	public Comment(User author, Post target, LocalDateTime creationTime, String description) {
		this.id = DigestUtils.sha256Hex(
				(author.getEmail() + target.getId() + creationTime.toString() + description)
						.getBytes(StandardCharsets.UTF_8));
		this.author = author;
		this.target = target;
		this.creationTime = creationTime;
		this.description = description;
	}
}
