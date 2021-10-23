package pl.edu.pg.zdziarski.lukasz.tweetbook.comment.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.model.CommentsModel;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.service.CommentService;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.PostService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@RequestScoped
@Named
public class CommentList implements Serializable {
    private CommentService commentService;
    private PostService postService;
    private CommentsModel comments;

    @Getter
    @Setter
    private String postId;

    @Inject
    public CommentList(PostService postService, CommentService commentService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    public void init() throws IOException {
        Optional<Post> post = postService.find(postId);
        if (post.isPresent()) {
            this.comments = CommentsModel.entityToModelMapper().apply(commentService.findByPost(post.get()));
        }
        else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
        }
    }

    public CommentsModel getComments() {
        if (comments == null) {
            comments = CommentsModel.entityToModelMapper().apply(commentService.findAll());
        }
        return comments;
    }
}
