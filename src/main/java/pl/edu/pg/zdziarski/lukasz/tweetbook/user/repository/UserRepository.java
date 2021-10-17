package pl.edu.pg.zdziarski.lukasz.tweetbook.user.repository;

import pl.edu.pg.zdziarski.lukasz.tweetbook.datastore.DataStore;
import pl.edu.pg.zdziarski.lukasz.tweetbook.repository.Repository;
import pl.edu.pg.zdziarski.lukasz.tweetbook.serialization.CloningUtility;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Dependent
public class UserRepository implements Repository<User, String> {
	private final DataStore store;

	@Inject
	public UserRepository(DataStore store) {
		this.store = store;
	}

	@Override
	public Optional<User> find(String email) {
		return store.findUser(email);
	}

	@Override
	public List<User> findAll() {
		return store.findAllUsers();
	}

	@Override
	public void create(User entity) {
		store.createUser(entity);
	}

	@Override
	public void delete(String email) {
		store.deleteUser(email);
	}

	@Override
	public void update(User entity) {
		User usr = CloningUtility.clone(entity);
		store.updateUser(entity);
	}

	public void deleteProfilePicture(User user) {
		User usr = CloningUtility.clone(user);
		usr.setProfilePicture(null);
		store.updateUser(usr);
	}
}
