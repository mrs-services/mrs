package mrs.app.reservation;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ReservationClientImpl implements ReservationClient {
	private final RestTemplate restTemplate;

	private final ParameterizedTypeReference<Resources<Reservation>> ref = new ParameterizedTypeReference<Resources<Reservation>>() {
	};

	public ReservationClientImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public Resources<Reservation> findByReservableRoomId(String reservableRoomId) {
		UriComponents uri = UriComponentsBuilder
				.fromHttpUrl("http://reservation")
				.pathSegment("v1", "reservations", "search",
						"findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc")
				.queryParam("reservableRoomId", reservableRoomId).build();
		return restTemplate.exchange(RequestEntity.get(uri.toUri()).build(), ref)
				.getBody();
	}

	@Override
	public void reserve(Reservation reservation) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("v1", "reservations").build();
		System.out.println(restTemplate.exchange(
				RequestEntity.post(uri.toUri()).body(reservation), JsonNode.class));
	}

	@Override
	public void cancel(Integer reservationId) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("v1", "reservations", reservationId.toString()).build();
		restTemplate.exchange(RequestEntity.delete(uri.toUri()).build(), Void.class);
	}
}
