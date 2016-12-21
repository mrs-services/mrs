package mrs;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ReservationSource {
	String RESERVE_OUTPUT = "reserve-output";
	String CANCEL_OUTPUT = "cancel-output";

	@Output(ReservationSource.RESERVE_OUTPUT)
	MessageChannel reserveOutput();

	@Output(ReservationSource.CANCEL_OUTPUT)
	MessageChannel cancelOutput();

}
