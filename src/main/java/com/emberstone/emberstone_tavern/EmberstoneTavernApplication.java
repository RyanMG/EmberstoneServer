package com.emberstone.emberstone_tavern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
