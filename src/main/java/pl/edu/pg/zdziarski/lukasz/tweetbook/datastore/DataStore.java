package pl.edu.pg.zdziarski.lukasz.tweetbook.datastore;

import lombok.extern.java.Log;
import pl.edu.pg.zdziarski.lukasz.tweetbook.serialization.CloningUtility;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log
@ApplicationScoped
public class DataStore {
	private Set<User> users;
	private Set<Post> posts;
	private Set<Comment> comments;

	public synchronized List<User> findAllUsers() {
		return users.stream()
				.map(CloningUtility::clone)
				.collect(Collectors.toList());
	}

	public synchronized Optional<User> findUser(String email) {
		return users.stream()
				.filter(user -> user.getEmail().equals(email))
				.findFirst()
				.map(CloningUtility::clone);
	}

	public synchronized void createUser(User user) throws IllegalArgumentException {
		findUser(user.getEmail()).ifPresentOrElse(
				original -> {
					throw new IllegalArgumentException(
							String.format("User with email \"%s\" already exists", user.getEmail())
					);
				},
				() -> users.add(CloningUtility.clone(user))
		);
	}

	public synchronized void deleteUser(String email) throws IllegalArgumentException {
		findUser(email).ifPresentOrElse(
				user -> {
					List<Post> userPosts = findPostsByAuthor(user);
					userPosts.forEach(post -> {
						deletePost(post.getId());
					});
					List<Comment> userComments = findCommentsByAuthor(user);
					userComments.forEach(comment -> {
						deleteComment(comment.getId());
					});
					users.remove(user);
				},
				() -> {
					throw new IllegalArgumentException(
							String.format("User with email \"%s\" does not exist", email)
					);
				}
		);
	}

	public synchronized List<Post> findAllPosts() {
		return posts.stream()
				.map(CloningUtility::clone)
				.collect(Collectors.toList());
	}

	public synchronized Optional<Post> findPost(String id) {
		return posts.stream()
				.filter(post -> post.getId().equals(id))
				.findFirst()
				.map(CloningUtility::clone);
	}

	public synchronized List<Post> findPostsByAuthor(User author) {
		return posts.stream()
				.filter(post -> post.getAuthor().equals(author))
				.collect(Collectors.toList());
	}

	public synchronized void createPost(Post post) throws IllegalArgumentException {
		findUser(post.getId()).ifPresentOrElse(
				original -> {
					throw new IllegalArgumentException(
							String.format("Post with id \"%s\" already exists", post.getId())
					);
				},
				() -> posts.add(CloningUtility.clone(post))
		);
	}

	public synchronized void deletePost(String id) throws IllegalArgumentException {
		findPost(id).ifPresentOrElse(
				original -> {
					List<Comment> comments = findCommentsByPost(original);
					comments.forEach(comment -> {
						deleteComment(comment.getId());
					});
					posts.remove(original);
				},
				() -> {
					throw new IllegalArgumentException(
							String.format("Post with id \"%s\" does not exist", id)
					);
				}
		);
	}

	public synchronized List<Comment> findAllComments() {
		return comments.stream()
				.map(CloningUtility::clone)
				.collect(Collectors.toList());
	}

	public synchronized Optional<Comment> findComment(String id) {
		return comments.stream()
				.filter(comment -> comment.getId().equals(id))
				.findFirst()
				.map(CloningUtility::clone);
	}

	public synchronized List<Comment> findCommentsByAuthor(User author) {
		return comments.stream()
				.filter(comment -> comment.getAuthor().equals(author))
				.collect(Collectors.toList());
	}

	public synchronized List<Comment> findCommentsByPost(Post post) {
		return comments.stream()
				.filter(comment -> comment.getTarget().equals(post))
				.collect(Collectors.toList());
	}

	public synchronized void createComment(Comment comment) throws IllegalArgumentException {
		findUser(comment.getId()).ifPresentOrElse(
				original -> {
					throw new IllegalArgumentException(
							String.format("Post with id \"%s\" already exists", comment.getId())
					);
				},
				() -> comments.add(CloningUtility.clone(comment))
		);
	}

	public synchronized void deleteComment(String id) throws IllegalArgumentException {
		findComment(id).ifPresentOrElse(
				comment -> comments.remove(comment),
				() -> {
					throw new IllegalArgumentException(
							String.format("Comment with id \"%s\" does not exist", id)
					);
				}
		);
	}
}
