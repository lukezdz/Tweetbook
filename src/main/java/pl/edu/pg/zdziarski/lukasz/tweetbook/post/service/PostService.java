package pl.edu.pg.zdziarski.lukasz.tweetbook.post.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.repository.PostRepository;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class PostService {
	private PostRepository repository;

	@Inject
	public PostService(PostRepository repository) {
		this.repository = repository;
	}

	public Optional<Post> find(String id) {
		return repository.find(id);
	}

	public List<Post> findAll() {
		return repository.findAll();
	}

	public List<Post> findAllByAuthor(User author) {
		return repository.findByAuthor(author);
	}

	public void create(Post post) {
		repository.create(post);
	}

	public void delete(String postId) {
		repository.delete(postId);
	}

	public void update(Post post) {
		repository.update(post);
	}
}
