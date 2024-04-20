package com.gmlsoftware.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
		info = @Info(
				title = "Prueba practica Java Spring OLSoftware",
				description = "Software de inventario, el cual permite el almacenamiento de equipos de cómputo,\r\n"
						+ "monitores y otros dispositivos como teclados, mouses, impresoras y bases para monitor o pantallas;\r\n"
						+ "también permite la gestión y parametrización de características y datos técnicos del dispositivo.",
				version = "0.0.1",
				license = @License(
						name = "Apache 2.0",
						url = "http://springdoc.org"
				)
		),
		security = {
				@SecurityRequirement(name = "bearerAuth")
		}
)
@SecurityScheme(
		name = "bearerAuth",
		description = "JWT auth description",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
