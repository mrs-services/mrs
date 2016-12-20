package mrs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientResponseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceErrorHandler extends DefaultResponseErrorHandler {
	private final ObjectMapper objectMapper;
	private static final Logger log = LoggerFactory.getLogger(ResourceErrorHandler.class);

	public ResourceErrorHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void handleError(ClientHttpResponse response) throws IOException {
		JsonNode body = objectMapper.readValue(response.getBody(), JsonNode.class);
		log.info("handle {}", body);
		if (body.has("path") && body.has("exception") && body.has("message")) {
			throw new RestClientResponseException(body.get("message").asText(),
					response.getRawStatusCode(), response.getStatusText(),
					response.getHeaders(),
					StreamUtils.copyToByteArray(response.getBody()),
					StandardCharsets.UTF_8);
		}
		else {
			super.handleError(response);
		}
	}
}
