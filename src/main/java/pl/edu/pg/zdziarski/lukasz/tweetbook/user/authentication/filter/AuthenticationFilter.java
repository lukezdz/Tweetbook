package pl.edu.pg.zdziarski.lukasz.tweetbook.user.authentication.filter;

import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.AuthMethods;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.HttpHeaders;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.authentication.AuthenticationUtility;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.servlet.ProfilePictureServlet;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.servlet.UserServlet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

//@WebFilter(urlPatterns = {
//		ProfilePictureServlet.Paths.PROFILE_PICTURES + "/*"
//})
public class AuthenticationFilter extends HttpFilter {
	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
		if (req.getSession().getAttribute("principal") == null) {
			//Check for authorization header with basic auth method
			String basic = req.getHeader(HttpHeaders.AUTHORIZATION);
			if (basic == null || !basic.startsWith(AuthMethods.BASIC)) {
				res.setHeader(HttpHeaders.WWW_AUTHENTICATE, String.format(AuthMethods.BASIC_REALM, "Tweetbook"));
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			//Parse credentials
			basic = basic.replace(AuthMethods.BASIC, "").trim();
			basic = new String(Base64.getDecoder().decode(basic));
			String[] credentials = basic.split(":");

			//Check credentials
			if (!AuthenticationUtility.authenticate(credentials[0], credentials[1], req)) {
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}
		chain.doFilter(req, res);
	}
}
