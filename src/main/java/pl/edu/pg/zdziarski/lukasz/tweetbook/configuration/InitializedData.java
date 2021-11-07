package pl.edu.pg.zdziarski.lukasz.tweetbook.configuration;

import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.entity.Comment;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.entity.Post;
import pl.edu.pg.zdziarski.lukasz.tweetbook.comment.service.CommentService;
import pl.edu.pg.zdziarski.lukasz.tweetbook.post.service.PostService;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.context.control.RequestContextController;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ApplicationScoped
public class InitializedData {
	private final UserService userService;
	private final PostService postService;
	private final CommentService commentService;
	private final RequestContextController requestContextController;

	@Inject
	public InitializedData(UserService userService, PostService postService,
						   CommentService commentService, RequestContextController requestContextController) {
		this.userService = userService;
		this.postService = postService;
		this.commentService = commentService;
		this.requestContextController = requestContextController;
	}

	public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {
		init();
	}

	private synchronized void init() {
		requestContextController.activate();
		User admin = User.builder()
				.email("admin@tweetbook.com")
				.nickname("Admin")
				.name("Admin")
				.surname("Realone")
				.birthday(LocalDate.of(1999, 01, 01))
				.password(DigestUtils.sha256Hex("adminadmin"))
				.build();
		User chad = User.builder()
				.email("chadchadwick@gmail.com")
				.nickname("BigBoy420")
				.name("Chad")
				.surname("Chadwick")
				.birthday(LocalDate.of(1998, 4, 20))
				.profilePicture(getResourceAsByteArray("profilePictures/chad.png"))
				.password(DigestUtils.sha256Hex("20.04.1998"))
				.build();
		User karen = User.builder()
				.email("karen.smith@o2.com")
				.nickname("CanISpeakWithTheManager?")
				.name("Karen")
				.surname("Smith")
				.birthday(LocalDate.of(1985, 7, 18))
				.profilePicture(getResourceAsByteArray("profilePictures/karen.png"))
				.password(DigestUtils.sha256Hex("Canispeakwiththemanager?!"))
				.build();
		User john = User.builder()
				.email("john12@gmail.com")
				.nickname("Johnny")
				.name("John")
				.surname("Kovalsky")
				.birthday(LocalDate.of(1965, 12, 1))
				.profilePicture(getResourceAsByteArray("profilePictures/john.png"))
				.password(DigestUtils.sha256Hex("12345"))
				.build();
		userService.create(admin);
		userService.create(chad);
		userService.create(karen);
		userService.create(john);

		Post adminAnnouncement = new Post(admin, "Please make sure to comply with TOS", LocalDateTime.now());
		Post chadHello = new Post(chad, "Hi all, I am Chad!", LocalDateTime.now());
		Post karenRant = new Post(karen, "Today I went to Walmart. -1/10 I will not come back. I had to ask for the manager twice!!!1!", LocalDateTime.now());
		Post johnBusiness = new Post(john, "I am John Kovalsky and I invite you to use the services of my accounting firm.", LocalDateTime.now());

		postService.create(adminAnnouncement);
		postService.create(chadHello);
		postService.create(karenRant);
		postService.create(johnBusiness);

		Comment johnToKaren = new Comment(john, karenRant, LocalDateTime.now(), "I assure you that at my accounting firm you would have to ask just once!");
		Comment chadToAdmin = new Comment(chad, adminAnnouncement, LocalDateTime.now(), "Yeah, yeah, sure");

		commentService.create(johnToKaren);
		commentService.create(chadToAdmin);

		requestContextController.deactivate();
	}

	@SneakyThrows
	private byte[] getResourceAsByteArray(String name) {
		try (InputStream is = this.getClass().getResourceAsStream(name)) {
			return is.readAllBytes();
		}
	}
}
