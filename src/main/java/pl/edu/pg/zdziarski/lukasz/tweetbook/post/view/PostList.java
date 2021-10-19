package pl.edu.pg.zdziarski.lukasz.tweetbook.post.view;

import pl.edu.pg.zdziarski.lukasz.tweetbook.post.model.PostsModel;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.PostService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@RequestScoped
@Named
public class PostList implements Serializable {
    private PostService postService;
    private PostsModel posts;


    @Inject
    public PostList(PostService postService) {
        this.postService = postService;
    }

    public PostsModel getPosts() {
        if (posts == null) {
            posts = PostsModel.entityToModelMapper().apply(postService.findAll());
        }
        return posts;
    }

    public String deleteAction(PostsModel.Post post) {
        postService.delete(post.getId());
        return "post_list?faces-redirect=true";
    }
}
