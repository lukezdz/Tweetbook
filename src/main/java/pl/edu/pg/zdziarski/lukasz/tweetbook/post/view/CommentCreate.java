package pl.edu.pg.zdziarski.lukasz.tweetbook.post.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.model.CommentCreateModel;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.model.PostModel;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.CommentService;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.PostService;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.model.UserModel;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.service.UserService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Named
@NoArgsConstructor
public class CommentCreate implements Serializable {
    private UserService userService;
    private PostService postService;
    private CommentService commentService;

    @Getter
    @Setter
    private CommentCreateModel comment;

    @Getter
    private List<PostModel> posts;

    @Getter
    private List<UserModel> users;

    @Inject
    public CommentCreate(UserService us, PostService ps, CommentService cs) {
        userService = us;
        postService = ps;
        commentService = cs;

        comment = new CommentCreateModel();
        comment.setDescription("");
    }

    @PostConstruct
    public void init() {
        posts = postService.findAll().stream().map(PostModel.entityToModelMapper()).collect(Collectors.toList());
        users = userService.findAll().stream().map(UserModel.entityToModelMapper()).collect(Collectors.toList());
    }

    public String saveAction() {
        commentService.create(CommentCreateModel.modelToEntityMapper(userService, postService).apply(comment));
        return "/post/post_view.xhtml?faces-redirect=true&id=" + comment.getPostId();
    }
}
