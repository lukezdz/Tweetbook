package pl.edu.pg.zdziarski.lukasz.tweetbook.post.repository;

import pl.edu.pg.zdziarski.lukasz.tweetbook.datastore.DataStore;
import pl.edu.pg.zdziarski.lukasz.tweetbook.repository.Repository;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Dependent
public class PostRepository implements Repository<Post, String> {
	private final DataStore store;

	@Inject
	public PostRepository(DataStore store) {
		this.store = store;
	}

	@Override
	public Optional<Post> find(String id) {
		return store.findPost(id);
	}

	@Override
	public List<Post> findAll() {
		return store.findAllPosts();
	}

	@Override
	public void create(Post entity) {
		store.createPost(entity);
	}

	@Override
	public void delete(String id) {
		store.deletePost(id);
	}

	@Override
	public void update(Post entity) {
		store.updatePost(entity);
	}

	public List<Post> findByAuthor(User user) {
		return store.findPostsByAuthor(user);
	}
}
