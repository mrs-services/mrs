package mrs.app.room;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("rooms")
public class RoomsController {
	private final ReservableRoomClient reservableRoomClient;
	private final MeetingRoomClient meetingRoomClient;

	public RoomsController(ReservableRoomClient reservableRoomClient,
			MeetingRoomClient meetingRoomClient) {
		this.reservableRoomClient = reservableRoomClient;
		this.meetingRoomClient = meetingRoomClient;
	}

	@GetMapping
	String listRooms(Model model) {
		LocalDate today = LocalDate.now();
		model.addAttribute("date", today);
		return listRooms(today, model);
	}

	@GetMapping(path = "{date}")
	String listRooms(
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
			Model model) {

		Set<Integer> reservableRoomIds = reservableRoomClient.findByReservedDate(date)
				.getContent().stream().map(x -> x.getReservableRoomId().getRoomId())
				.collect(Collectors.toSet());
		List<MeetingRoom> meetingRooms = meetingRoomClient.findAll().getContent().stream()
				.filter(x -> reservableRoomIds.contains(x.getRoomId()))
				.collect(Collectors.toList());
		model.addAttribute("rooms", meetingRooms);
		return "rooms/listRooms";
	}
}
