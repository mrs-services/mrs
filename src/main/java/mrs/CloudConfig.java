package mrs;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@Profile("cloud")
public class CloudConfig extends AbstractCloudConfig {
	@Bean
	RedisConnectionFactory redisConnectionFactory() {
		return connectionFactory().redisConnectionFactory();
	}

}