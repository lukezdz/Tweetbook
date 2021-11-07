package pl.edu.pg.zdziarski.lukasz.tweetbook.user.repository;

import pl.edu.pg.zdziarski.lukasz.tweetbook.repository.Repository;
import pl.edu.pg.zdziarski.lukasz.tweetbook.serialization.CloningUtility;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class UserRepository implements Repository<User, String> {
	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager entityManager) {
		this.em = entityManager;
	}

	@Override
	public Optional<User> find(String email) {
		return Optional.ofNullable(em.find(User.class, email));
	}

	@Override
	public List<User> findAll() {
		return em.createQuery("select u from User u", User.class).getResultList();
	}

	@Override
	public void create(User entity) {
		em.persist(entity);
	}

	@Override
	public void delete(String email) {
		em.remove(em.find(User.class, email));
	}

	@Override
	public void update(User entity) {
		em.merge(entity);
	}

	@Override
	public void detach(User entity) {
		em.detach(entity);
	}

	public void deleteProfilePicture(User user) {
		User usr = CloningUtility.clone(user);
		usr.setProfilePicture(null);
		em.merge(usr);
	}
}
