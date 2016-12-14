package mrs.app.room;

import java.time.LocalDate;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ReservableRoomClientImpl implements ReservableRoomClient {

	private final RestTemplate restTemplate;
	private final ParameterizedTypeReference<Resources<ReservableRoom>> ref = new ParameterizedTypeReference<Resources<ReservableRoom>>() {
	};

	public ReservableRoomClientImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public Resources<ReservableRoom> findAll() {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("api", "reservableRooms").build();
		return restTemplate.exchange(RequestEntity.get(uri.toUri()).build(), ref)
				.getBody();
	}

	@Override
	public Resources<ReservableRoom> findByReservedDate(LocalDate reservedDate) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("api", "reservableRooms", "search",
						"findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc")
				.queryParam("reservedDate", reservedDate).build();
		return restTemplate.exchange(RequestEntity.get(uri.toUri()).build(), ref)
				.getBody();
	}
}
