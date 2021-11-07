package pl.edu.pg.zdziarski.lukasz.tweetbook.comment.repository;

import pl.edu.pg.zdziarski.lukasz.tweetbook.repository.Repository;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class CommentRepository implements Repository<Comment, String> {
	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager entityManager) {
		this.em = entityManager;
	}

	@Override
	public Optional<Comment> find(String id) {
		return Optional.ofNullable(em.find(Comment.class, id));
	}

	@Override
	public List<Comment> findAll() {
		return em.createQuery("select c from Comment c", Comment.class).getResultList();
	}

	@Override
	public void create(Comment entity) {
		em.persist(entity);
	}

	@Override
	public void delete(String id) {
		em.remove(em.find(Comment.class, id));
	}

	@Override
	public void update(Comment entity) {
		em.merge(entity);
	}

	@Override
	public void detach(Comment entity) {
		em.detach(entity);
	}

	public List<Comment> findByPost(Post post) {
		return em.createQuery("select c from Comment c where c.target.id = :postId", Comment.class)
				.setParameter("postId", post.getId())
				.getResultList();
	}

	public List<Comment> findByAuthor(User author) {
		return em.createQuery("select c from Comment c where c.author.email = :email", Comment.class)
				.setParameter("email", author.getEmail())
				.getResultList();
	}
}
