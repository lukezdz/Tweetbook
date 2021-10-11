package pl.edu.pg.zdziarski.lukasz.tweetbook.user.authentication.servlet;

import pl.edu.pg.zdziarski.lukasz.tweetbook.user.authentication.service.AuthenticationService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MultipartConfig
@WebServlet(urlPatterns = {
		AuthenticationServlet.Paths.LOGIN,
		AuthenticationServlet.Paths.LOGOUT
})
public class AuthenticationServlet extends HttpServlet {

	private final AuthenticationService service;

	@Inject
	public AuthenticationServlet(AuthenticationService service) {
		this.service = service;
	}

	public static class Paths {
		public static final String LOGIN = "/api/user/login";
		public static final String LOGOUT = "/api/user/logout";
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String servletPath = req.getServletPath();

		if (Paths.LOGIN.equals(servletPath)) {
			if (!service.authenticate(req.getParameter("email"), req.getParameter("password"))) {
				resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		else if (Paths.LOGOUT.equals(servletPath)) {
			req.getSession().invalidate();
		}
		else {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}

