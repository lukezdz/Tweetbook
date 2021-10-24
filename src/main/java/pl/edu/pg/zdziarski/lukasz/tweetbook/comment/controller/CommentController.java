package pl.edu.pg.zdziarski.lukasz.tweetbook.comment.controller;

import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.dto.CreateCommentRequest;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.dto.GetCommentResponse;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.dto.GetCommentsResponse;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.dto.UpdateCommentRequest;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.service.CommentService;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.PostService;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Optional;

@Path("")
public class CommentController {
	private UserService userService;
	private PostService postService;
	private CommentService commentService;

	@Inject
	public void setServices(UserService userService, PostService postService, CommentService commentService) {
		this.userService = userService;
		this.postService = postService;
		this.commentService = commentService;
	}

	@GET
	@Path("/comments")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComments() {
		return Response.ok(GetCommentsResponse.entityToDtoMapper().apply(commentService.findAll())).build();
	}

	@GET
	@Path("/posts/{id}/comments")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPostComments(@PathParam("id") String postId) {
		Optional<Post> post = postService.find(postId);
		if (post.isPresent()) {
			return Response.ok(
					GetCommentsResponse.entityToDtoMapper()
							.apply(commentService.findByPost(post.get())))
					.build();
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@GET
	@Path("/comments/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComment(@PathParam("id") String id) {
		Optional<Comment> comment = commentService.find(id);
		if (comment.isPresent()) {
			return Response.ok(GetCommentResponse.entityToDtoMapper().apply(comment.get())).build();
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@POST
	@Path("/posts/{id}/comments")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createComment(CreateCommentRequest request, @PathParam("id") String postId) {
		Optional<User> user = userService.find(request.getAuthorEmail());
		Optional<Post> post = postService.find(postId);

		if (user.isPresent() && post.isPresent()) {
			Comment comment = CreateCommentRequest.dtoToEntityMapper(userService, postService, postId).apply(request);
			commentService.create(comment);
			return Response.created(
					UriBuilder.fromMethod(CommentController.class, "getComment")
							.build(comment.getId()))
					.build();
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@Path("/comments/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateComment(UpdateCommentRequest request, @PathParam("id") String id) {
		Optional<Comment> comment = commentService.find(id);
		if (comment.isPresent()) {
			comment.get().setDescription(request.getDescription());
			commentService.update(comment.get());
			return Response.noContent().build();
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@DELETE
	@Path("/comments/{id}")
	public Response deleteComment(@PathParam("id") String id) {
		Optional<Comment> comment = commentService.find(id);
		if (comment.isPresent()) {
			commentService.delete(id);
			return Response.accepted().build();
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}
}
