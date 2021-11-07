package pl.edu.pg.zdziarski.lukasz.tweetbook.post.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
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
public class PostService {
	private UserRepository userRepository;
	private PostRepository postRepository;

	@Inject
	public PostService(UserRepository userRepository, PostRepository postRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	public Optional<Post> find(String id) {
		return postRepository.find(id);
	}

	public List<Post> findAll() {
		return postRepository.findAll();
	}

	public List<Post> findAllByAuthor(User author) {
		return postRepository.findByAuthor(author);
	}

	@Transactional
	public void create(Post post) {
		postRepository.create(post);
		userRepository.find(post.getAuthor().getEmail()).ifPresent(user -> user.getPosts().add(post));
	}

	@Transactional
	public void delete(String postId) {
		Post post = postRepository.find(postId).orElseThrow();
		userRepository.find(post.getAuthor().getEmail()).ifPresent(user -> user.getPosts().remove(post));
		postRepository.delete(postId);
	}

	@Transactional
	public void update(Post post) {
		Post original = postRepository.find(post.getId()).orElseThrow();
		postRepository.detach(original);
		postRepository.update(post);
	}
}
