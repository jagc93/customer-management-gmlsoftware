package com.gmlsoftware.util;

import static com.gmlsoftware.config.logger.LoggerConfig.error;
import static com.gmlsoftware.config.logger.LoggerConfig.logger;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.gmlsoftware.service.UseLogger;

@Component
public class ObjectMapperUtil implements UseLogger {

	private final ObjectMapper objectMapper;
	private final Logger log;

	public ObjectMapperUtil(ObjectMapper _objectMapper) {
		this.objectMapper = _objectMapper;
		this.log = logger(ObjectMapperUtil.class);
	}

	public <T> String writeValueAsString(T object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException jpe) {
			error(log, jpe);
			throw new IllegalArgumentException(jpe);
		}
	}

	public <T> T readValue(String str, TypeReference<T> tr) {
		try {
			return objectMapper.readValue(str, tr);
		} catch (JsonProcessingException jpe) {
			error(log, jpe);
			throw new IllegalArgumentException(jpe);
		}
	}

	public <T> T readValue(String str, Class<T> clazz) {
		try {
			return objectMapper.readValue(str, clazz);
		} catch (JsonProcessingException jpe) {
			error(log, jpe);
			throw new IllegalArgumentException(jpe);
		}
	}
}
