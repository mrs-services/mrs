package mrs.app.notification;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NotificationController {
	private final NotificationClient notificationClient;

	public NotificationController(NotificationClient notificationClient) {
		this.notificationClient = notificationClient;
	}

	@GetMapping("notifications/{notificationId}/delete")
	String delete(@PathVariable("notificationId") String notificationId) {
		notificationClient.delete(notificationId);
		return "redirect:/";
	}
}
