package mrs.app.reservation;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "reservation", path = "api/reservations")
public interface ReservationClient {
	@RequestMapping(method = RequestMethod.GET, value = "search/findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc")
	Resources<Reservation> findByReservableRoomId(
			@RequestParam("reservableRoomId") String reservableRoomId);

	@RequestMapping(method = RequestMethod.POST)
	void reserve(@RequestBody Reservation reservation);

	@RequestMapping(method = RequestMethod.DELETE, value = "{reservationId}")
	void cancel(@PathVariable("reservationId") Integer reservationId);
}
