package mrs.app.room;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class MeetingRoomClientImpl implements MeetingRoomClient {

	private final RestTemplate restTemplate;
	private final ParameterizedTypeReference<Resources<MeetingRoom>> ref = new ParameterizedTypeReference<Resources<MeetingRoom>>() {
	};

	public MeetingRoomClientImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public MeetingRoom findOne(Integer roomId) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("api", "meetingRooms", String.valueOf(roomId)).build();
		return restTemplate
				.exchange(RequestEntity.get(uri.toUri()).build(), MeetingRoom.class)
				.getBody();
	}

	@Override
	public Resources<MeetingRoom> findAll() {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("api", "meetingRooms").build();
		return restTemplate.exchange(RequestEntity.get(uri.toUri()).build(), ref)
				.getBody();
	}
}
