package mrs.app.room;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class MeetingRoomClientImpl implements MeetingRoomClient {

	private final RestTemplate restTemplate;
	private final ParameterizedTypeReference<Resources<MeetingRoom>> ref = new ParameterizedTypeReference<Resources<MeetingRoom>>() {
	};
	private final static Logger log = LoggerFactory
			.getLogger(MeetingRoomClientImpl.class);

	public MeetingRoomClientImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	@HystrixCommand(fallbackMethod = "findOneFallback")
	public MeetingRoom findOne(Integer roomId) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("v1", "meetingRooms", String.valueOf(roomId)).build();
		return restTemplate
				.exchange(RequestEntity.get(uri.toUri()).build(), MeetingRoom.class)
				.getBody();
	}

	public MeetingRoom findOneFallback(Integer roomId) {
		log.warn("findOneFallback({})", roomId);
		return new MeetingRoom(-1, "Error");
	}

	@Override
	@HystrixCommand(fallbackMethod = "findAllFallback")
	public Resources<MeetingRoom> findAll() {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("v1", "meetingRooms").build();
		return restTemplate.exchange(RequestEntity.get(uri.toUri()).build(), ref)
				.getBody();
	}

	public Resources<MeetingRoom> findAllFallback() {
		log.warn("findAllFallback()");
		return new Resources<>(Collections.singletonList(new MeetingRoom(-1, "Error")));
	}
}
