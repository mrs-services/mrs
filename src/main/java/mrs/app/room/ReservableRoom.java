package mrs.app.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class ReservableRoom implements Serializable {
	private final Id reservableRoomId;

	@JsonCreator
	public ReservableRoom(@JsonProperty("reservableRoomId") Id reservableRoomId) {
		this.reservableRoomId = reservableRoomId;
	}

	public Id getReservableRoomId() {
		return reservableRoomId;
	}

	@Override
	public String toString() {
		return "ReservableRoom{" + "reservableRoomId='" + reservableRoomId + '\'' + '}';
	}

	public static class Id {
		private final Integer roomId;
		private final LocalDate reservedDate;

		@JsonCreator
		public Id(@JsonProperty("roomId") Integer roomId,
				@JsonProperty("reservedDate") LocalDate reservedDate) {
			this.roomId = roomId;
			this.reservedDate = reservedDate;
		}

		public Integer getRoomId() {
			return roomId;
		}

		public LocalDate getReservedDate() {
			return reservedDate;
		}

		@Override
		public String toString() {
			return roomId + "_" + reservedDate;
		}
	}
}
