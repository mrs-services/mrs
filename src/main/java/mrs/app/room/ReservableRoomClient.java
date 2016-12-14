package mrs.app.room;

import java.time.LocalDate;

import org.springframework.hateoas.Resources;

public interface ReservableRoomClient {
	Resources<ReservableRoom> findAll();

	Resources<ReservableRoom> findByReservedDate(LocalDate reservedDate);
}
