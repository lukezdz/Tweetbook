package pl.edu.pg.zdziarski.lukasz.tweetbook.post.model.converter;

import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.model.PostModel;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.PostService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.util.Optional;

@FacesConverter(forClass = PostModel.class, managed = true)
public class PostModelConverter implements Converter<PostModel> {
    private final PostService postService;

    @Inject
    public PostModelConverter(PostService postService) {
        this.postService = postService;
    }

    @Override
    public PostModel getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        Optional<Post> post = postService.find(s);
        return post.isEmpty() ? null : PostModel.entityToModelMapper().apply(post.get());
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, PostModel postModel) {
        return postModel == null ? "" : postModel.getId();
    }
}
