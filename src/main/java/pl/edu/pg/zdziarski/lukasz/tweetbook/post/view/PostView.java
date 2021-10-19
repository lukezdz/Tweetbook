package pl.edu.pg.zdziarski.lukasz.tweetbook.post.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.model.CommentsModel;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.model.PostModel;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.model.PostsModel;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.CommentService;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.PostService;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@ViewScoped
@Named
public class PostView implements Serializable {
    private final PostService postService;
    private final CommentService commentService;

    @Getter
    @Setter
    private String id;

    @Getter
    private PostModel post;

    @Getter
    private CommentsModel comments;

    @Inject
    public PostView(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    public void init() throws IOException {
        Optional<Post> _post = postService.find(id);

        if (_post.isPresent()) {
            this.comments = CommentsModel.entityToModelMapper().apply(commentService.findByPost(_post.get()));
            post = PostModel.entityToModelMapper().apply(_post.get());
        }
        else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
        }

        for (CommentsModel.Comment comment : comments.getComments()) {
            System.out.println(comment.toString());
        }
    }

    public String deleteAction(String id) {
        commentService.delete(id);
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }
}
