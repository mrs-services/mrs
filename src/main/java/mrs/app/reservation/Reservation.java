package mrs.app.reservation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalTime;

public class Reservation implements Serializable {
	private Integer reservationId;
	private final LocalTime startTime;
	private final LocalTime endTime;
	private String userId;
	private String reservableRoom;

	@JsonCreator
	public Reservation(@JsonProperty("startTime") LocalTime startTime,
			@JsonProperty("endTime") LocalTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}

	public Integer getReservationId() {
		return reservationId;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setReservableRoom(String reservableRoom) {
		this.reservableRoom = reservableRoom;
	}

	public String getReservableRoom() {
		return reservableRoom;
	}

	@Override
	public String toString() {
		return "Reservation{" + "reservationId=" + reservationId + ", startTime="
				+ startTime + ", endTime=" + endTime + ", userId='" + userId + '\''
				+ ", reservableRoom='" + reservableRoom + '\'' + '}';
	}
}
