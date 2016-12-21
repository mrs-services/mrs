package mrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication
@EnableOAuth2Sso
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableBinding(ReservationSource.class)
public class MrsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MrsApplication.class, args);
	}

	@Bean
	public static ConfigureRedisAction configureRedisAction() {
		return ConfigureRedisAction.NO_OP;
	}

	@Bean
	Java8TimeDialect java8TimeDialect() {
		return new Java8TimeDialect();
	}

	@Configuration
	static class MvcConfig extends WebMvcConfigurerAdapter {
		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("/").setViewName("redirect:/rooms");
		}
	}

	@Bean
	@LoadBalanced
	OAuth2RestTemplate restTemplate(OAuth2ClientContext oauth2ClientContext,
			OAuth2ProtectedResourceDetails resource,
			HttpMessageConverters messageConverters, ObjectMapper objectMapper) {
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource,
				oauth2ClientContext);
		// Set same message converters as RestTemplate to use Jackson2HalModule
		restTemplate.setMessageConverters(messageConverters.getConverters());
		// Handle non-OAuth format error
		restTemplate.setErrorHandler(new ResourceErrorHandler(objectMapper));
		return restTemplate;
	}

	@Bean
	Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
		return new Jackson2ObjectMapperBuilder()
				.modulesToInstall(new Jackson2HalModule(), new JavaTimeModule())
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
}
