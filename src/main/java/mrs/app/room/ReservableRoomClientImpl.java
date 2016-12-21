package mrs.app.room;

import java.time.LocalDate;
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
public class ReservableRoomClientImpl implements ReservableRoomClient {

	private final RestTemplate restTemplate;
	private final ParameterizedTypeReference<Resources<ReservableRoom>> ref = new ParameterizedTypeReference<Resources<ReservableRoom>>() {
	};
	private final static Logger log = LoggerFactory
			.getLogger(ReservableRoomClientImpl.class);

	public ReservableRoomClientImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	@HystrixCommand(fallbackMethod = "findAllFallback")
	public Resources<ReservableRoom> findAll() {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("v1", "reservableRooms").build();
		return restTemplate.exchange(RequestEntity.get(uri.toUri()).build(), ref)
				.getBody();
	}

	public Resources<ReservableRoom> findAllFallback() {
		log.warn("findAllFallback()");
		return new Resources<>(Collections.emptyList());
	}

	@Override
	@HystrixCommand(fallbackMethod = "findByReservedDateFallback")
	public Resources<ReservableRoom> findByReservedDate(LocalDate reservedDate) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://reservation")
				.pathSegment("v1", "reservableRooms", "search", "findByReservedDate")
				.queryParam("reservedDate", reservedDate).build();
		return restTemplate.exchange(RequestEntity.get(uri.toUri()).build(), ref)
				.getBody();
	}

	public Resources<ReservableRoom> findByReservedDateFallback(LocalDate reservedDate) {
		log.warn("findByReservedDateFallback({})", reservedDate);
		ReservableRoom reservableRoom = new ReservableRoom(
				new ReservableRoom.Id(-1, reservedDate));
		return new Resources<>(Collections.singletonList(reservableRoom));
	}
}
