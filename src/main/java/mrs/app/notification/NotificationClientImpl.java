package mrs.app.notification;

import static org.springframework.http.RequestEntity.get;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class NotificationClientImpl implements NotificationClient {
	private final RestTemplate restTemplate;
	private final ParameterizedTypeReference<Resources<Notification>> ref = new ParameterizedTypeReference<Resources<Notification>>() {
	};
	private final static Logger log = LoggerFactory
			.getLogger(NotificationClientImpl.class);

	public NotificationClientImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	@HystrixCommand(fallbackMethod = "findByUserIdFallback")
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

	public Resources<Notification> findByUserIdFallback(String userId) {
		log.warn("findByUserIdFallback({})", userId);
		return new Resources<>(Collections.emptyList());
	}

	@Override
	@HystrixCommand(fallbackMethod = "deleteFallback")
	public void delete(String notificationId) {
		restTemplate.exchange(RequestEntity.delete(fromHttpUrl("http://notification")
				.pathSegment("v1", "notifications", notificationId).build().toUri())
				.build(), Void.class);
	}

	public void deleteFallback(String notificationId) {
		log.warn("deleteFallback({})", notificationId);
	}

}
