package mrs.app.room;

import org.springframework.hateoas.Resources;

public interface MeetingRoomClient {
	MeetingRoom findOne(Integer roomId);

	Resources<MeetingRoom> findAll();
}
