package mrs.app.room;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "reservation", path = "api/reservableRooms")
public interface ReservableRoomClient {
	@RequestMapping(method = RequestMethod.GET)
	Resources<ReservableRoom> findAll();

	@RequestMapping(method = RequestMethod.GET, value = "search/findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc")
	Resources<ReservableRoom> findByReservedDate(
			@RequestParam("reservedDate") LocalDate reservedDate);
}
