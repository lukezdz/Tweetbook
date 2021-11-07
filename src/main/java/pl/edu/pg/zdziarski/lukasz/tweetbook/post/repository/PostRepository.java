package pl.edu.pg.zdziarski.lukasz.tweetbook.post.repository;

import pl.edu.pg.zdziarski.lukasz.tweetbook.repository.Repository;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class PostRepository implements Repository<Post, String> {
	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager entityManager) {
		this.em = entityManager;
	}

	@Override
	public Optional<Post> find(String id) {
		return Optional.ofNullable(em.find(Post.class, id));
	}

	@Override
	public List<Post> findAll() {
		return em.createQuery("select p from Post p", Post.class).getResultList();
	}

	@Override
	public void create(Post entity) {
		em.persist(entity);
	}

	@Override
	public void delete(String id) {
		em.remove(em.find(Post.class, id));
	}

	@Override
	public void update(Post entity) {
		em.merge(entity);
	}

	@Override
	public void detach(Post entity) {
		em.detach(entity);
	}

	public List<Post> findByAuthor(User user) {
		return em.createQuery("select p from Post p where p.author.email = :email", Post.class)
				.setParameter("email", user.getEmail())
				.getResultList();
	}
}
