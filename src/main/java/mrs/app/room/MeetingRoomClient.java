package mrs.app.room;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "reservation", path = "api/meetingRooms")
public interface MeetingRoomClient {
	@RequestMapping(method = RequestMethod.GET, value = "{roomId}")
	MeetingRoom findOne(@PathVariable("roomId") Integer roomId);

	@RequestMapping(method = RequestMethod.GET)
	Resources<MeetingRoom> findAll();
}
