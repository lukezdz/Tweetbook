package pl.edu.pg.zdziarski.lukasz.tweetbook.user.authentication.service;

import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.context.UserContext;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class AuthenticationService {
	private UserService service;

	private UserContext context;

	@Inject
	public AuthenticationService(UserService service, UserContext context) {
		this.service = service;
		this.context = context;
	}

	public boolean authenticate(String email, String password) {
		Optional<User> user = service.find(email, DigestUtils.sha256Hex(password));
		if (user.isPresent()) {
			context.setPrincipal(user.get().getEmail());
			return true;
		}
		else {
			return false;
		}
	}
}
