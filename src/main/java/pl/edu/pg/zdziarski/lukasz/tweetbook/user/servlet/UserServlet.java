package pl.edu.pg.zdziarski.lukasz.tweetbook.user.servlet;

import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.HttpHeaders;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.MimeTypes;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.ServletUtility;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.UrlFactory;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.dto.CreateUserRequest;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.dto.GetUserResponse;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.dto.GetUsersResponse;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.dto.UpdateUserRequest;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.service.UserService;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = { UserServlet.Paths.USER + "/*"})
public class UserServlet extends HttpServlet {
	private final UserService userService;

	@Inject
	public UserServlet(UserService userService) {
		this.userService = userService;
	}

	public static class Paths {
		public static final String USER = "/api/users";
	}

	public static class Patterns {
		public static final String USERS = "^/?$";
		public static final String USER = "^/[a-z0-9@.]+/?$";
	}

	private final Jsonb jsonb = JsonbBuilder.create();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.USER.equals(servletPath)) {
			if (path.matches(Patterns.USER)) {
				getUser(req, resp);
				return;
			}
			else if (path.matches(Patterns.USERS)) {
				getUsers(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		if (Paths.USER.equals(req.getServletPath())) {
			if (path.matches(Patterns.USER)) {
				postUser(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		if (Paths.USER.equals(req.getServletPath())) {
			if (path.matches(Patterns.USER)) {
				putUser(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		if (Paths.USER.equals(req.getServletPath())) {
			if (path.matches(Patterns.USER)) {
				deleteUser(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	private void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<User> user = userService.find(email);

		if (user.isPresent()) {
			response.setContentType(MimeTypes.APPLICATION_JSON);
			response.getWriter().write(jsonb.toJson(GetUserResponse.entityToDtoMapper().apply(user.get())));
		}
		else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void getUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType(MimeTypes.APPLICATION_JSON);
		response.getWriter().write(jsonb.toJson(GetUsersResponse.entityToDtoMapper().apply(userService.findAll())));
	}

	private void postUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CreateUserRequest requestBody = jsonb.fromJson(request.getInputStream(), CreateUserRequest.class);
		User user = CreateUserRequest.dtoToEntityMapper().apply(requestBody);

		try {
			userService.create(user);
			response.addHeader(HttpHeaders.LOCATION, UrlFactory.createUrl(request, Paths.USER, user.getEmail()));
			response.setStatus(HttpServletResponse.SC_CREATED);
		}
		catch (IllegalArgumentException ex) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void putUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<User> user = userService.find(email);

		if (user.isPresent()) {
			UpdateUserRequest requestBody = jsonb.fromJson(request.getInputStream(), UpdateUserRequest.class);
			UpdateUserRequest.dtoToEntityMapper().apply(user.get(), requestBody);

			userService.update(user.get());
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<User> user = userService.find(email);

		if (user.isPresent()) {
			userService.delete(user.get().getEmail());
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
