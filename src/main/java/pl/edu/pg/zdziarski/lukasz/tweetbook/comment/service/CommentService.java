package pl.edu.pg.zdziarski.lukasz.tweetbook.comment.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.repository.CommentRepository;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class CommentService {
	private CommentRepository repository;

	@Inject
	public CommentService(CommentRepository repository) {
		this.repository = repository;
	}

	public Optional<Comment> find(String id) {
		return repository.find(id);
	}

	public List<Comment> findAll() {
		return repository.findAll();
	}

	public List<Comment> findByAuthor(User author) {
		return repository.findByAuthor(author);
	}

	public List<Comment> findByPost(Post post) {
		return repository.findByPost(post);
	}

	public void create(Comment comment) {
		repository.create(comment);
	}

	public void delete(String commentId) {
		repository.delete(commentId);
	}

	public void update(Comment comment) {
		repository.update(comment);
	}
}
