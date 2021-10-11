package pl.edu.pg.zdziarski.lukasz.tweetbook.servlet;

import javax.servlet.http.HttpServletRequest;

public class UrlFactory {

	/**
	 * Creates URL using host, port and context root from servlet request and any number of path elements. If any of
	 * path elements starts or ends with '/' character, that character is removed.
	 *
	 * @param request servlet request
	 * @param paths   any (can be none) number of path elements
	 * @return created url
	 */
	public static String createUrl(HttpServletRequest request, String... paths) {
		StringBuilder builder = new StringBuilder();
		builder.append(request.getScheme())
				.append("://")
				.append(request.getServerName())
				.append(":")
				.append(request.getServerPort())
				.append(request.getContextPath());
		for (String path : paths) {
			builder.append("/")
					.append(path, path.startsWith("/") ? 1 : 0, path.endsWith("/") ? path.length() - 1: path.length());
		}
		return builder.toString();
	}
}
