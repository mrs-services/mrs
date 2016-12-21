package mrs.app.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class AccessToken {
	private final String token;
	public static final String MESSAGE_HEADER = "accessToken";

	public AccessToken() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		OAuth2Authentication auth = OAuth2Authentication.class.cast(authentication);
		OAuth2AuthenticationDetails details = OAuth2AuthenticationDetails.class
				.cast(auth.getDetails());
		this.token = details.getTokenValue();
	}

	AccessToken(String token) {
		this.token = token;
	}

	public String value() {
		return token;
	}
}
