package pl.edu.pg.zdziarski.lukasz.tweetbook.user.servlet;

import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.HttpHeaders;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.MimeTypes;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.ServletUtility;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.service.UserService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet(urlPatterns = ProfilePictureServlet.Paths.PROFILE_PICTURES + "/*")
@MultipartConfig(maxFileSize =  500 * 1024)
public class ProfilePictureServlet extends HttpServlet {
	private final UserService service;

	@Inject
	public ProfilePictureServlet(UserService service) {
		this.service = service;
	}

	public static class Paths {
		public static final String PROFILE_PICTURES = "/api/profile-pictures";
	}

	/**
	 * Definition of regular expression patterns supported by this servlet. Separate inner class provides composition
	 * for static fields. Whereas servlet activation path can be compared to {@link Paths} the path info (denoted by
	 * wildcard in paths) can be compared using regular expressions.
	 */
	public static class Patterns {
		/**
		 * Specified portrait (for download).
		 */
		public static final String PROFILE_PICTURE = "^/\\S+@\\S+\\.\\S+/?$";
	}

	/**
	 * Request parameters (both query params and request parts) which can be sent by the client.
	 */
	public static class Parameters {
		/**
		 * Portrait image part.
		 */
		public static final String PROFILE_PICTURE = "profile-picture";
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.PROFILE_PICTURES.equals(servletPath)) {
			if (path.matches(Patterns.PROFILE_PICTURE)) {
				getProfilePicture(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.PROFILE_PICTURES.equals(servletPath)) {
			if (path.matches(Patterns.PROFILE_PICTURE)) {
				putProfilePicture(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.PROFILE_PICTURES.equals(servletPath)) {
			if (path.matches(Patterns.PROFILE_PICTURE)) {
				postProfilePicture(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.PROFILE_PICTURES.equals(servletPath)) {
			if (path.matches(Patterns.PROFILE_PICTURE)) {
				deleteProfilePicture(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	private void getProfilePicture(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<User> user = service.find(email);

		if (user.isPresent()) {
			response.addHeader(HttpHeaders.CONTENT_TYPE, MimeTypes.IMAGE_PNG);
			if (user.get().getProfilePicture() != null) {
				response.setContentLength(user.get().getProfilePicture().length);
				response.getOutputStream().write(user.get().getProfilePicture());
			}
			else {
				response.setContentLength(0);
				response.getOutputStream().write("".getBytes(StandardCharsets.UTF_8));
			}
		}
		else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void putProfilePicture(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String email = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<User> user = service.find(email);

		if (user.isPresent()) {
			Part profilePicture = request.getPart(Parameters.PROFILE_PICTURE);
			if (profilePicture != null) {
				service.updateProfilePicture(email, profilePicture.getInputStream());
			}
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void postProfilePicture(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String email = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<User> user = service.find(email);

		if (user.isPresent()) {
			if (user.get().getProfilePicture() == null) {
				Part profilePicture = request.getPart(Parameters.PROFILE_PICTURE);
				if (profilePicture != null) {
					service.updateProfilePicture(email, profilePicture.getInputStream());
				}
				response.setStatus(HttpServletResponse.SC_OK);
			}
			else {
				response.setStatus(HttpServletResponse.SC_CONFLICT);
			}
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void deleteProfilePicture(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String email = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<User> user = service.find(email);

		if (user.isPresent()) {
			service.deleteProfilePicture(user.get().getEmail());
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
