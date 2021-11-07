package pl.edu.pg.zdziarski.lukasz.tweetbook.user.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class UserService {
	private UserRepository repository;

	@Inject
	public UserService(UserRepository repository) {
		this.repository = repository;
	}

	public Optional<User> find(String email) {
		return repository.find(email);
	}

	public Optional<User> find(String email, String password) {
		Optional<User> user = repository.find(email);
		if (user.isPresent() && user.get().getPassword().equals(password)) {
			return user;
		}
		return Optional.empty();
	}

	public List<User> findAll() {
		return repository.findAll();
	}

	@Transactional
	public void create(User user) {
		repository.create(user);
	}

	@Transactional
	public void delete(String userEmail) {
		repository.delete(userEmail);
	}

	@Transactional
	public void update(User user) {
		repository.update(user);
	}

	@Transactional
	public void updateProfilePicture(String email, InputStream is) {
		repository.find(email).ifPresent(user -> {
			try {
				user.setProfilePicture(is.readAllBytes());
				repository.update(user);
			}
			catch (IOException ex) {
				throw new IllegalStateException(ex);
			}
		});
	}

	@Transactional
	public void deleteProfilePicture(String email) {
		repository.deleteProfilePicture(repository.find(email).orElseThrow());
	}
}
