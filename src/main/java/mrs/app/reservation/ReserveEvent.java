package mrs.app.reservation;

import java.time.LocalTime;

public class ReserveEvent {
	private String reservableRoomId;
	private LocalTime startTime;
	private LocalTime endTime;

	public ReserveEvent(String reservableRoomId, LocalTime startTime, LocalTime endTime) {
		this.reservableRoomId = reservableRoomId;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	ReserveEvent() {
	}

	public String getReservableRoomId() {
		return reservableRoomId;
	}

	public void setReservableRoomId(String reservableRoomId) {
		this.reservableRoomId = reservableRoomId;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getType() {
		return "reserve";
	}
}
