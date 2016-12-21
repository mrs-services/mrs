package mrs.app.reservation;

import org.springframework.hateoas.Resources;

public interface ReservationClient {
	Resources<Reservation> findByReservableRoomId(String reservableRoomId);

	void checkReservation(Reservation reservation);

	void reserve(Reservation reservation);

	void cancel(Integer reservationId);
}
