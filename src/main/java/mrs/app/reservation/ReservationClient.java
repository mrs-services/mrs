package mrs.app.reservation;

import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReservationClient {
	Resources<Reservation> findByReservableRoomId(
			@RequestParam("reservableRoomId") String reservableRoomId);

	void reserve(@RequestBody Reservation reservation);

	void cancel(@PathVariable("reservationId") Integer reservationId);
}
