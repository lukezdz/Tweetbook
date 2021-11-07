package pl.edu.pg.zdziarski.lukasz.tweetbook.comment.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.repository.CommentRepository;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.repository.PostRepository;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class CommentService {
	private UserRepository userRepository;
	private PostRepository postRepository;
	private CommentRepository commentRepository;

	@Inject
	public CommentService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
	}

	public Optional<Comment> find(String id) {
		return commentRepository.find(id);
	}

	public List<Comment> findAll() {
		return commentRepository.findAll();
	}

	public List<Comment> findByAuthor(User author) {
		return commentRepository.findByAuthor(author);
	}

	public List<Comment> findByPost(Post post) {
		return commentRepository.findByPost(post);
	}

	@Transactional
	public void create(Comment comment) {
		commentRepository.create(comment);
		postRepository.find(comment.getTarget().getId()).ifPresent(post -> post.getComments().add(comment));
		userRepository.find(comment.getAuthor().getEmail()).ifPresent(user -> user.getComments().add(comment));
	}

	@Transactional
	public void delete(String commentId) {
		Comment comment = commentRepository.find(commentId).orElseThrow();
		postRepository.find(comment.getTarget().getId()).ifPresent(post -> post.getComments().remove(comment));
		userRepository.find(comment.getAuthor().getEmail()).ifPresent(user -> user.getComments().remove(comment));
		commentRepository.delete(commentId);
	}

	@Transactional
	public void update(Comment comment) {
		Comment original = commentRepository.find(comment.getId()).orElseThrow();
		commentRepository.detach(original);
		commentRepository.update(comment);
	}
}
