package com.gmlsoftware.config;

import java.text.SimpleDateFormat;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

	private String standardDateFormatter;

	@Bean("simpleDateFormatStandard")
	public SimpleDateFormat sdf() {
		return new SimpleDateFormat(standardDateFormatter);
	}
}
