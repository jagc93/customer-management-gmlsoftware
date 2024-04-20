package com.gmlsoftware.entity.listener;

import static com.gmlsoftware.config.logger.LoggerConfig.info;
import static com.gmlsoftware.config.logger.LoggerConfig.logger;
import static com.gmlsoftware.constant.GlobalConstant.LOG_BEFORE_CREATED;
import static com.gmlsoftware.constant.GlobalConstant.LOG_BEFORE_UPDATED;
import static com.gmlsoftware.constant.GlobalConstant.LOG_CREATE_END;
import static com.gmlsoftware.constant.GlobalConstant.LOG_UPDATE_END;

import org.apache.log4j.Logger;

import com.gmlsoftware.util.ObjectMapperUtil;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class GenericListener {

	protected final ObjectMapperUtil objectMapper;
	protected final Logger log;

	protected GenericListener(ObjectMapperUtil _objectMapper, Class<?> clazz) {
		this.objectMapper = _objectMapper;
		this.log = logger(clazz);
	}

	@PrePersist
    public void beforeCreate(Object entity) {
		addLog(LOG_BEFORE_CREATED, entity);
    }

    @PreUpdate
    public void beforeUpdate(Object entity) {
    	addLog(LOG_BEFORE_UPDATED, entity);
    }

    @PostPersist
    public void afterCreate(Object entity) {
    	addLog(LOG_CREATE_END, entity);
    }

    @PostUpdate
    public void afterUpdate(Object entity) {
    	addLog(LOG_UPDATE_END, entity);
    }

    protected void addLog(String message, Object entity) {
    	info(log, String.format(message, entity.getClass().getSimpleName(), objectMapper.writeValueAsString(entity)));
    }
}
