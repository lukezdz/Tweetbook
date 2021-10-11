package pl.edu.pg.zdziarski.lukasz.tweetbook.servlet;

import javax.servlet.http.HttpServletRequest;

public class ServletUtility {
	public static String parseRequestPath(HttpServletRequest request) {
		String path = request.getPathInfo();
		path = path != null ? path : "";
		return path;
	}
}
