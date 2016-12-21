package mrs.app.notification;

import static org.springframework.http.RequestEntity.get;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationClient {
	private final RestTemplate restTemplate;
	private final ParameterizedTypeReference<Resources<Notification>> ref = new ParameterizedTypeReference<Resources<Notification>>() {
	};

	public NotificationClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public Resources<Notification> findByUserId(String userId) {
		return restTemplate
				.exchange(
						get(fromHttpUrl("http://notification")
								.pathSegment("v1", "notifications", "search",
										"findByUserId")
								.queryParam("userId", userId).build().toUri()).build(),
						ref)
				.getBody();
	}

	public void delete(String notificationId) {
		restTemplate.exchange(RequestEntity.delete(fromHttpUrl("http://notification")
				.pathSegment("v1", "notifications", notificationId).build().toUri())
				.build(), Void.class);
	}

}
