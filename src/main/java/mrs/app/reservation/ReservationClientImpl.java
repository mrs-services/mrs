package mrs.app.reservation;

import static org.springframework.http.RequestEntity.*;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import mrs.app.auth.AccessToken;

@Component
public class ReservationClientImpl implements ReservationClient {
	private final RestTemplate restTemplate;
	private final Source source;
	private final AccessToken accessToken;

	private final ParameterizedTypeReference<Resources<Reservation>> ref = new ParameterizedTypeReference<Resources<Reservation>>() {
	};
	private static final Logger log = LoggerFactory
			.getLogger(ReservationClientImpl.class);

	public ReservationClientImpl(RestTemplate restTemplate, Source source,
			AccessToken accessToken) {
		this.restTemplate = restTemplate;
		this.source = source;
		this.accessToken = accessToken;
	}

	@Override
	public Resources<Reservation> findByReservableRoomId(String reservableRoomId) {
		return restTemplate
				.exchange(get(fromHttpUrl("http://reservation")
						.pathSegment("v1", "reservations", "search",
								"findByReservableRoomId")
						.queryParam("reservableRoomId", reservableRoomId).build().toUri())
								.build(),
						ref)
				.getBody();
	}

	@Override
	public void reserve(Reservation reservation) {
		checkReservation(reservation);
		Message<ReserveEvent> message = MessageBuilder
				.withPayload(new ReserveEvent(reservation.getReservableRoom(),
						reservation.getStartTime(), reservation.getEndTime()))
				.setHeader(AccessToken.MESSAGE_HEADER, accessToken.value()).build();
		source.output().send(message);
	}

	public void checkReservation(Reservation reservation) {
		log.info("check reservation {}", reservation);
		restTemplate.exchange(post(fromHttpUrl("http://reservation")
				.pathSegment("v1", "reservations", "check").build().toUri())
						.body(reservation),
				Void.class);
	}

	@Override
	public void cancel(Integer reservationId) {
		log.info("cancel reservation {}", reservationId);
		restTemplate.exchange(delete(fromHttpUrl("http://reservation")
				.pathSegment("v1", "reservations", String.valueOf(reservationId)).build()
				.toUri()).build(), Void.class);
	}
}
