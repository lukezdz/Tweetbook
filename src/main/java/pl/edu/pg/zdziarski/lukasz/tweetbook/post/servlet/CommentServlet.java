package pl.edu.pg.zdziarski.lukasz.tweetbook.post.servlet;

import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.CreateCommentRequest;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.GetCommentResponse;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.GetCommentsResponse;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.UpdateCommentRequest;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.CommentService;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.PostService;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.HttpHeaders;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.MimeTypes;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.ServletUtility;
import pl.edu.pg.zdziarski.lukasz.tweetbook.servlet.UrlFactory;
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

@WebServlet(urlPatterns = {CommentServlet.Paths.COMMENTS + "/*", CommentServlet.Paths.POST_COMMENTS + "/*"})
public class CommentServlet extends HttpServlet {
	private final UserService userService;
	private final PostService postService;
	private final CommentService commentService;
	private final Jsonb jsonb;

	@Inject
	public CommentServlet(UserService userService, PostService postService, CommentService commentService) {
		this.userService = userService;
		this.postService = postService;
		this.commentService = commentService;
		this.jsonb = JsonbBuilder.create();
	}

	public static class Paths {
		public static final String COMMENTS = "/api/comments";
		public static final String POST_COMMENTS = "/api/post/comments";
	}

	public static class Patterns {
		public static final String COMMENTS = "^/?$";
		public static final String COMMENT = "/[a-zA-Z0-9]+/?$";
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.COMMENTS.matches(servletPath)) {
			if (path.matches(Patterns.COMMENT)) {
				getComment(req, resp);
				return;
			}
			if (path.matches(Patterns.COMMENTS)) {
				getComments(req, resp);
				return;
			}
		}
		else if (Paths.POST_COMMENTS.equals(servletPath)) {
			if (path.matches(Patterns.COMMENT)) {
				getPostComments(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.COMMENTS.matches(servletPath)) {
			if (path.matches(Patterns.COMMENT)) {
				postComment(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.COMMENTS.matches(servletPath)) {
			if (path.matches(Patterns.COMMENT)) {
				putComment(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtility.parseRequestPath(req);
		String servletPath = req.getServletPath();

		if (Paths.COMMENTS.matches(servletPath)) {
			if (path.matches(Patterns.COMMENT)) {
				deleteComment(req, resp);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	private void getComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<Comment> comment = commentService.find(id);

		if (comment.isPresent()) {
			response.setContentType(MimeTypes.APPLICATION_JSON);
			response.getWriter().write(jsonb.toJson(GetCommentResponse.entityToDtoMapper().apply(comment.get())));
		}
		else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void getComments(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType(MimeTypes.APPLICATION_JSON);
		response.getWriter().write(jsonb.toJson(GetCommentsResponse.entityToDtoMapper().apply(commentService.findAll())));
	}

	private void getPostComments(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String postId = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<Post> post = postService.find(postId);

		if (post.isPresent()) {
			response.setContentType(MimeTypes.APPLICATION_JSON);
			response.getWriter().write(jsonb.toJson(GetCommentsResponse.entityToDtoMapper().apply(commentService.findByPost(post.get()))));
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void postComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CreateCommentRequest requestBody = jsonb.fromJson(request.getInputStream(), CreateCommentRequest.class);
		Comment comment = CreateCommentRequest.dtoToEntityMapper(userService, postService).apply(requestBody);

		try {
			commentService.create(comment);
			response.addHeader(HttpHeaders.LOCATION, UrlFactory.createUrl(request, Paths.COMMENTS, comment.getId()));
			response.setStatus(HttpServletResponse.SC_CREATED);
		}
		catch (IllegalArgumentException ex) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void putComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<Comment> comment = commentService.find(id);

		if (comment.isPresent()) {
			UpdateCommentRequest requestBody = jsonb.fromJson(request.getInputStream(), UpdateCommentRequest.class);
			UpdateCommentRequest.dtoToEntityMapper().apply(comment.get(), requestBody);

			commentService.update(comment.get());
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void deleteComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = ServletUtility.parseRequestPath(request).replaceAll("/", "");
		Optional<Comment> comment = commentService.find(id);

		if (comment.isPresent()) {
			commentService.delete(comment.get().getId());
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
