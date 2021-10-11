package pl.edu.pg.zdziarski.lukasz.tweetbook.user.authentication;

import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class AuthenticationUtility {
	/**
	 * Checks if login and password are correct and if yes then stores user in session context.
	 *
	 * @param email    user's email
	 * @param password user's password
	 * @param request  current http request
	 * @return true if user was successfully authenticated
	 */
	public static boolean authenticate(String email, String password, HttpServletRequest request) {
		UserService service = (UserService) request.getServletContext().getAttribute("userService");
		Optional<User> user = service.find(email, DigestUtils.sha256Hex(password.getBytes(StandardCharsets.UTF_8)));
		if (user.isPresent()) {
			//Put username in session where it will be present while session is valid.
			request.getSession().setAttribute("principal", user.get().getEmail());
			return true;
		} else {
			return false;
		}
	}
}
