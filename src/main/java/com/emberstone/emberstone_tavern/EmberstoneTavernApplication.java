package com.emberstone.emberstone_tavern;

import com.emberstone.emberstone_tavern.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class EmberstoneTavernApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmberstoneTavernApplication.class, args);
	}

	@Component
	public class WebConfig implements WebMvcConfigurer {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**")
					.allowedOrigins("*")
					.allowedMethods("GET","POST", "PUT", "DELETE", "OPTIONS", "HEAD");
		}

	}
}
