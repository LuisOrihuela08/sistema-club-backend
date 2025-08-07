package com.club.control.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfiguration {
	
	@Value("${frontend.url}")
	private String frontendUrl;

	@Bean
	public WebMvcConfigurer corsConfiguration() {
		return new WebMvcConfigurer() {
			
			@Override
			public void addCorsMappings(CorsRegistry corsRegistry) {
				corsRegistry.addMapping("/**")
							.allowedOrigins(frontendUrl)
							.allowedMethods("GET", "POST", "PUT", "DELETE")
							.allowedHeaders("*");
			}
		};
	}
}
