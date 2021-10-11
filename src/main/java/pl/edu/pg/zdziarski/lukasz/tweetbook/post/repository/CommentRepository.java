package pl.edu.pg.zdziarski.lukasz.tweetbook.post.repository;

import pl.edu.pg.zdziarski.lukasz.tweetbook.datastore.DataStore;
import pl.edu.pg.zdziarski.lukasz.tweetbook.repository.Repository;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import java.util.List;
import java.util.Optional;

public class CommentRepository implements Repository<Comment, String> {
	private final DataStore store;

	public CommentRepository(DataStore store) {
		this.store = store;
	}

	@Override
	public Optional<Comment> find(String id) {
		return store.findComment(id);
	}

	@Override
	public List<Comment> findAll() {
		return store.findAllComments();
	}

	@Override
	public void create(Comment entity) {
		store.createComment(entity);
	}

	@Override
	public void delete(String id) {
		store.deleteComment(id);
	}

	@Override
	public void update(Comment entity) {
		throw new UnsupportedOperationException("Not implemented.");
	}

	public List<Comment> findByPost(Post post) {
		return store.findCommentsByPost(post);
	}

	public List<Comment> findByAuthor(User author) {
		return store.findCommentsByAuthor(author);
	}
}