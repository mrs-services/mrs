package mrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignFormatterRegistrar;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@SpringBootApplication
@EnableOAuth2Sso
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
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
	RequestInterceptor oauth2FeignRequestInterceptor(
			OAuth2ClientContext oauth2ClientContext,
			OAuth2ProtectedResourceDetails resource) {
		return new OAuth2FeignRequestInterceptor(oauth2ClientContext, resource);
	}

	@Bean
	InitializingBean messageConvertersInitializer(
			HttpMessageConverters messageConverters) {
		return () -> messageConverters.getConverters().stream()
				.filter(c -> c instanceof MappingJackson2HttpMessageConverter).findAny()
				.ifPresent(c -> {
					MappingJackson2HttpMessageConverter converter = (MappingJackson2HttpMessageConverter) c;
					ObjectMapper objectMapper = converter.getObjectMapper();
					objectMapper.registerModule(new Jackson2HalModule());
				});
	}

	@Bean
	FeignFormatterRegistrar localDateFeignFormatterRegistrar() {
		return formatterRegistry -> {
			DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
			registrar.setUseIsoFormat(true);
			registrar.registerFormatters(formatterRegistry);
		};
	}

}
