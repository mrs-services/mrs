package mrs.app.reservation;

import java.io.Serializable;

public class CancelEvent implements Serializable {
	private Integer reservationId;

	public CancelEvent(Integer reservationId) {
		this.reservationId = reservationId;
	}

	CancelEvent() {
	}

	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}

	public String getType() {
		return "cancel";
	}
}
