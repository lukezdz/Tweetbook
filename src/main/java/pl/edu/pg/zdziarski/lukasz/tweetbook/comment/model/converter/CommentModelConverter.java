package pl.edu.pg.zdziarski.lukasz.tweetbook.comment.model.converter;

import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.model.CommentModel;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.service.CommentService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.util.Optional;

@FacesConverter(forClass = CommentModel.class, managed = true)
public class CommentModelConverter implements Converter<CommentModel> {
    private final CommentService commentService;

    @Inject
    public CommentModelConverter(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public CommentModel getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        Optional<Comment> comment = commentService.find(s);
        return comment.isEmpty() ? null : CommentModel.entityToModelMapper().apply(comment.get());
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, CommentModel commentModel) {
        return commentModel == null ? "" : commentModel.getId();
    }
}
