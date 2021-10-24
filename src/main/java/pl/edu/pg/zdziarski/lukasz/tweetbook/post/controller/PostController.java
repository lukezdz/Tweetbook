package pl.edu.pg.zdziarski.lukasz.tweetbook.post.controller;

import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.CreatePostRequest;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.GetPostResponse;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.GetPostsResponse;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.UpdatePostRequest;
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
import java.time.LocalDateTime;
import java.util.Optional;

@Path("")
public class PostController {
	private PostService postService;
	private UserService userService;

	public PostController() {

	}

	@Inject
	public void setPostService(PostService postService) {
		this.postService = postService;
	}

	@Inject
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@GET
	@Path("/posts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPosts() {
		return Response
				.ok(GetPostsResponse.entityToDtoMapper().apply(postService.findAll()))
				.build();
	}

	@GET
	@Path("/posts/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPost(@PathParam("id") String id) {
		Optional<Post> post = postService.find(id);
		if (post.isPresent()) {
			return Response
					.ok(GetPostResponse.entityToDtoMapper().apply(post.get()))
					.build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@POST
	@Path("/posts")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPost(CreatePostRequest request) {
		Optional<User> user = userService.find(request.getAuthorsEmail());
		if (user.isPresent()) {
			Post post = new Post(user.get(), request.getDescription(), LocalDateTime.now());
			postService.create(post);
			return Response.created(
					UriBuilder.fromMethod(PostController.class, "getPost")
							.build(post.getId()))
					.build();
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@Path("/posts/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePost(UpdatePostRequest request, @PathParam("id") String id) {
		Optional<Post> post = postService.find(id);
		if (post.isPresent()) {
			post.get().setDescription(request.getDescription());
			postService.update(post.get());
			return Response.noContent().build();
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@DELETE
	@Path("/posts/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePost(@PathParam("id") String id) {
		Optional<Post> post = postService.find(id);
		if (post.isPresent()) {
			postService.delete(id);
			return Response.accepted().build();
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}
}
