package mrs.app.reservation;

import static org.springframework.http.RequestEntity.get;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import mrs.ReservationSource;
import mrs.app.auth.AccessToken;

@Component
public class ReservationClientImpl implements ReservationClient {
	private final RestTemplate restTemplate;
	private final ReservationSource reservationSource;
	private final AccessToken accessToken;

	private final ParameterizedTypeReference<Resources<Reservation>> ref = new ParameterizedTypeReference<Resources<Reservation>>() {
	};
	private static final Logger log = LoggerFactory
			.getLogger(ReservationClientImpl.class);

	public ReservationClientImpl(RestTemplate restTemplate,
			ReservationSource reservationSource, AccessToken accessToken) {
		this.restTemplate = restTemplate;
		this.reservationSource = reservationSource;
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
	public void checkReservation(Reservation reservation) {
		log.info("check reservation {}", reservation);
		restTemplate.exchange(post(fromHttpUrl("http://reservation")
				.pathSegment("v1", "reservations", "check").build().toUri())
						.body(reservation),
				Void.class);
	}

	@Override
	public void reserve(Reservation reservation) {
		log.info("reserve {}", reservation);
		Message<ReserveEvent> message = MessageBuilder
				.withPayload(new ReserveEvent(reservation.getReservableRoom(),
						reservation.getStartTime(), reservation.getEndTime()))
				.setHeader(AccessToken.MESSAGE_HEADER, accessToken.value()).build();
		reservationSource.reserveOutput().send(message);
	}

	@Override
	public void cancel(Integer reservationId) {
		log.info("cancel reservation {}", reservationId);
		Message<CancelEvent> message = MessageBuilder
				.withPayload(new CancelEvent(reservationId))
				.setHeader(AccessToken.MESSAGE_HEADER, accessToken.value()).build();
		reservationSource.cancelOutput().send(message);
	}
}
