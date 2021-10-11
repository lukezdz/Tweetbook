package pl.edu.pg.zdziarski.lukasz.tweetbook.configuration;

import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.entity.User;
import pl.edu.pg.zdziarski.lukasz.tweetbook.user.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@ApplicationScoped
public class InitializedData {
	private final UserService userService;

	@Inject
	public InitializedData(UserService userService) {
		this.userService = userService;
	}

	public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {
		init();
	}

	private synchronized void init() {
		User admin = User.builder()
				.email("admin@tweetbook.com")
				.nickname("Admin")
				.name("Admin")
				.surname("Realone")
				.birthday(LocalDate.of(1999, 01, 01))
				.profilePicture(getResourceAsByteArray("profilePictures/admin.png"))
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
	}

	@SneakyThrows
	private byte[] getResourceAsByteArray(String name) {
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(name)) {
			if (is != null) {
				return is.readAllBytes();
			}
			else {
				return "".getBytes(StandardCharsets.UTF_8);
			}
		}
	}
}
