package mrs.app.reservation;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
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
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("api", "reservations", "search",
						"findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc")
				.build();
		return restTemplate.exchange(
				RequestEntity.get(uri.toUri()).accept(MediaTypes.HAL_JSON).build(), ref)
				.getBody();
	}

	@Override
	public void reserve(Reservation reservation) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("api", "reservations").build();
		try {
			restTemplate.exchange(RequestEntity.post(uri.toUri()).body(reservation),
					Void.class);
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void cancel(Integer reservationId) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("api", "reservations", reservationId.toString()).build();
		restTemplate.exchange(RequestEntity.delete(uri.toUri()).build(), Void.class);
	}
}
