package pl.edu.pg.zdziarski.lukasz.tweetbook.post.service;

import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.repository.CommentRepository;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import java.util.List;
import java.util.Optional;

public class CommentService {
	private final CommentRepository repository;

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
}
