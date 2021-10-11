package pl.edu.pg.zdziarski.lukasz.tweetbook.user.context;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class UserContext implements Serializable {
	@Getter
	@Setter
	private String principal;

	public boolean isAuthorized() {
		return principal != null;
	}
}
