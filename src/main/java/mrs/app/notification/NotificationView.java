package mrs.app.notification;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodName;

import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

@Component
public class NotificationView {

	private final NotificationClient notificationClient;

	public NotificationView(NotificationClient notificationClient) {
		this.notificationClient = notificationClient;
	}

	public String list(String userId) {
		StringBuilder sb = new StringBuilder();
		Resources<Notification> notifications = notificationClient.findByUserId(userId);
		if (notifications.iterator().hasNext()) {
			sb.append("<ul>");
			notifications.forEach(notification -> {
				sb.append("<li>[");
				sb.append(notification.getNotificationType());
				sb.append("] ");
				sb.append(notification.getNotificationMessage());
				sb.append(" @ ");
				sb.append(notification.getCreatedAt().toString());
				sb.append(" [<a href=\"");
				sb.append(fromMethodName(NotificationController.class, "delete",
						notification.getNotificationId()).build().encode().toString());
				sb.append("\">X</a>]");
				sb.append("</li>");
			});
			sb.append("</ul>");
		}
		return sb.toString();
	}
}
