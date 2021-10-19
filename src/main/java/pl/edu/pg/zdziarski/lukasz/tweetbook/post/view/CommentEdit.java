package pl.edu.pg.zdziarski.lukasz.tweetbook.post.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.model.CommentEditModel;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.CommentService;

import javax.enterprise.context.RequestScoped;
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
public class CommentEdit implements Serializable {
    private CommentService commentService;

    @Getter
    @Setter
    private String commentId;

    @Getter
    private CommentEditModel comment;

    @Inject
    public CommentEdit(CommentService commentService) {
        this.commentService = commentService;
    }

    public void init() throws IOException {
        Optional<Comment> comment = commentService.find(commentId);
        if (comment.isPresent()) {
            this.comment = CommentEditModel.entityToModelMapper().apply(comment.get());
        }
        else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Comment does not exist");
        }
    }

    public String saveAction() {
        Optional<Comment> _comment = commentService.find(commentId);
        if (_comment.isPresent()) {
            commentService.update(CommentEditModel.modelToEntityMapper().apply(_comment.get(), comment));
            String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
            return viewId + "?faces-redirect=true&includeViewParams=true";
        }
        return "";
    }
}
