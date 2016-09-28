package mrs.app.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MeetingRoom implements Serializable {
	private final Integer roomId;
	private final String roomName;

	@JsonCreator
	public MeetingRoom(@JsonProperty("roomId") Integer roomId,
			@JsonProperty("roomProperty") String roomName) {
		this.roomId = roomId;
		this.roomName = roomName;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	@Override
	public String toString() {
		return "MeetingRoom{" + "roomId=" + roomId + ", roomName='" + roomName + '\''
				+ '}';
	}
}
