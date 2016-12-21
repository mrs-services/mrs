package mrs.app.notification;

import org.springframework.hateoas.Resources;

public interface NotificationClient {
	Resources<Notification> findByUserId(String userId);

	void delete(String notificationId);
}
