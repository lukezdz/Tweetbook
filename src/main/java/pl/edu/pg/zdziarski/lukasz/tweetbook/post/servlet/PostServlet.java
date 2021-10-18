package pl.edu.pg.zdziarski.lukasz.tweetbook.post.servlet;

import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.CreatePostRequest;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.GetPostResponse;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.GetPostsResponse;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.UpdatePostRequest;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.PostService;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.HttpHeaders;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.MimeTypes;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.ServletUtility;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.UrlFactory;
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

@WebServlet(urlPatterns = { PostServlet.Paths.POST + "/*", PostServlet.Paths.USER_POSTS + "/*"})
public class PostServlet extends HttpServlet {
	private final UserService userService;
	private final PostService postService;
	private final Jsonb jsonb;

	@Inject
	public PostServlet(UserService userService, PostService postService) {
		this.userService = userService;
		this.postService = postService;
		this.jsonb = JsonbBuilder.create();
	}

	public static class Paths {
		public static final String POST = "/api/posts";
		public static final String USER_POSTS = "/api/user/posts";
	}

	public static class Patterns {
		public static final String POSTS = "^/?$";
		public static final String POST = "^/[a-zA-Z0-9]+/?$";
		public static final String USER_POSTS = "^/[a-z@.]+/?$";
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.POST.equals(servletPath)) {
			if (path.matches(Patterns.POST)) {
				getPost(req, resp);
				return;
			}

			if (path.matches(Patterns.POSTS)) {
				getPosts(req, resp);
				return;
			}
		}
		else if (Paths.USER_POSTS.equals(servletPath)) {
			if (path.matches(Patterns.USER_POSTS)) {
				getUserPosts(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.POST.equals(servletPath)) {
			if (path.matches(Patterns.POSTS)) {
				postPost(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.POST.equals(servletPath)) {
			if (path.matches(Patterns.POST)) {
				putPost(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.POST.equals(servletPath)) {
			if (path.matches(Patterns.POST)) {
				deletePost(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	private void getPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<Post> post = postService.find(id);

		if (post.isPresent()) {
			response.setContentType(MimeTypes.APPLICATION_JSON);
			response.getWriter().write(jsonb.toJson(GetPostResponse.entityToDtoMapper().apply(post.get())));
		}
		else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void getPosts(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType(MimeTypes.APPLICATION_JSON);
		response.getWriter().write(jsonb.toJson(GetPostsResponse.entityToDtoMapper().apply(postService.findAll())));
	}

	private void getUserPosts(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<User> user = userService.find(email);

		if (user.isPresent()) {
			response.setContentType(MimeTypes.APPLICATION_JSON);
			response.getWriter().write(jsonb.toJson(GetPostsResponse.entityToDtoMapper().apply(postService.findAllByAuthor(user.get()))));
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void postPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CreatePostRequest requestBody = jsonb.fromJson(request.getInputStream(), CreatePostRequest.class);
		Post post = CreatePostRequest.dtoToEntityMapper(userService).apply(requestBody);

		try {
			postService.create(post);
			response.addHeader(HttpHeaders.LOCATION, UrlFactory.createUrl(request, Paths.POST, post.getId()));
			response.setStatus(HttpServletResponse.SC_CREATED);
		}
		catch (IllegalArgumentException ex) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void putPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<Post> post = postService.find(id);

		if (post.isPresent()) {
			UpdatePostRequest updatePostRequest = jsonb.fromJson(request.getInputStream(), UpdatePostRequest.class);
			UpdatePostRequest.dtoToEntityMapper().apply(post.get(), updatePostRequest);

			postService.update(post.get());
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void deletePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<Post> post = postService.find(id);

		if (post.isPresent()) {
			postService.delete(post.get().getId());
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}