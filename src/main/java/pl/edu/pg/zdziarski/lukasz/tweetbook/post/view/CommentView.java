package pl.edu.pg.zdziarski.lukasz.tweetbook.post.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.model.CommentModel;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.CommentService;
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
public class CommentView implements Serializable {
    private final CommentService commentService;
    private final PostService postService;

    @Getter
    @Setter
    private String id;

    @Getter
    private CommentModel comment;

    @Inject
    public CommentView(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    public void init() throws IOException {
        Optional<Comment> _comment = commentService.find(id);
        if (_comment.isPresent()) {
            comment = CommentModel.entityToModelMapper().apply(_comment.get());
        }
        else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Comment not found");
        }
    }
}
