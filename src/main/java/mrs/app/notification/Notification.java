package mrs.app.notification;

import java.io.Serializable;
import java.time.Instant;

public class Notification implements Serializable {
	private String notificationId;
	private String notificationType;
	private String notificationMessage;
	private String userId;
	private Instant createdAt;

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getNotificationMessage() {
		return notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Notification{" +
				"notificationId='" + notificationId + '\'' +
				", notificationType='" + notificationType + '\'' +
				", notificationMessage='" + notificationMessage + '\'' +
				", userId='" + userId + '\'' +
				", createdAt=" + createdAt +
				'}';
	}
}
