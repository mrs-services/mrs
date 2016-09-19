package mrs;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.sql.DataSource;

@Configuration
@Profile("cloud")
public class CloudConfig extends AbstractCloudConfig {

	@Bean
	DataSource dataSource() {
		return connectionFactory().dataSource(new DataSourceConfig(
				new PooledServiceConnectorConfig.PoolConfig(0, 2, 3000), null));
	}

	@Bean
	RedisConnectionFactory redisConnectionFactory() {
		return connectionFactory().redisConnectionFactory();
	}

}