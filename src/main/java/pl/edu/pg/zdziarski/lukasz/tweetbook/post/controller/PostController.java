package pl.edu.pg.zdziarski.lukasz.tweetbook.post.controller;

import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.GetPostResponse;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.dto.GetPostsResponse;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.PostService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/posts")
public class PostController {
	private PostService postService;

	public PostController() {

	}

	@Inject
	public void setPostService(PostService postService) {
		this.postService = postService;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPosts() {
		return Response
				.ok(GetPostsResponse.entityToDtoMapper().apply(postService.findAll()))
				.build();
	}

	@GET
	@Path("{id}")
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
}
