package com.gmlsoftware.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
		info = @Info(
				title = "Prueba practica Java Spring GMLSoftware",
				description = "Este microservicio se encarga de todos los aspectos relacionados con la administración de clientes.",
				version = "0.0.1",
				license = @License(
						name = "Apache 2.0",
						url = "http://springdoc.org"
				)
		)
)
public class OpenApiConfig {

}
