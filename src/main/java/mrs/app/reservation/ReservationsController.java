package mrs.app.reservation;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Resources;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import mrs.app.room.MeetingRoomClient;
import mrs.app.room.ReservableRoom;

@Controller
@RequestMapping("reservations/{date}/{roomId}")
public class ReservationsController {
	private final ReservationClient reservationClient;
	private final MeetingRoomClient meetingRoomClient;
	private final LoadBalancerClient loadBalancerClient;

	public ReservationsController(ReservationClient reservationClient,
			MeetingRoomClient meetingRoomClient, LoadBalancerClient loadBalancerClient) {
		this.reservationClient = reservationClient;
		this.meetingRoomClient = meetingRoomClient;
		this.loadBalancerClient = loadBalancerClient;
	}

	@ModelAttribute
	ReservationForm setUpForm() {
		ReservationForm form = new ReservationForm();
		// デフォルト値
		form.setStartTime(LocalTime.of(9, 0));
		form.setEndTime(LocalTime.of(10, 0));
		return form;
	}

	@GetMapping
	String reserveForm(
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
			@PathVariable("roomId") Integer roomId, Model model, Principal principal) {
		ReservableRoom.Id reservableRoomId = new ReservableRoom.Id(roomId, date);
		LocalTime baseTime = LocalTime.of(0, 0);
		List<LocalTime> timeList = IntStream.range(0, 24 * 2)
				.mapToObj(i -> baseTime.plusMinutes(30 * i)).collect(Collectors.toList());
		model.addAttribute("room", meetingRoomClient.findOne(roomId));
		Resources<Reservation> reservations = reservationClient
				.findByReservableRoomId(reservableRoomId.toString());
		model.addAttribute("reservations", reservations.getContent());
		model.addAttribute("timeList", timeList);
		return "reservations/reserveForm";
	}

	@PostMapping
	String reserve(@Validated ReservationForm form, BindingResult bindingResult,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
			@PathVariable("roomId") Integer roomId, Model model, Principal principal) {
		if (bindingResult.hasErrors()) {
			return reserveForm(date, roomId, model, principal);
		}
		try {
			Reservation reservation = new Reservation(form.getStartTime(),
					form.getEndTime());
			String reservableRoom = UriComponentsBuilder
					.fromUri(loadBalancerClient.choose("reservation").getUri())
					.pathSegment("v1", "reservableRooms",
							new ReservableRoom.Id(roomId, date).toString()).toUriString();
			reservation.setReservableRoom(reservableRoom);
			reservationClient.checkReservation(reservation);
			reservationClient.reserve(reservation);
		}
		catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			return reserveForm(date, roomId, model, principal);
		}
		return "redirect:/reservations/{date}/{roomId}";
	}

	@PostMapping(params = "cancel")
	String cancel(@RequestParam("reservationId") Integer reservationId,
			@PathVariable("roomId") Integer roomId,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
			Model model, Principal principal) {
		try {
			reservationClient.cancel(reservationId);
		}
		catch (AccessDeniedException e) {
			model.addAttribute("error", e.getMessage());
			return reserveForm(date, roomId, model, principal);
		}
		return "redirect:/reservations/{date}/{roomId}";
	}
}
