package com.club.control.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Backend -  Sistema Club Campestre - API")
											.version("1.0.0")
											.description("Esta documentación es para el sistema de gestión para club campestres")
											.summary("Desarrollado por Luis Orihuela"));
											
	}
}
