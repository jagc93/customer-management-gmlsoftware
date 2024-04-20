package com.gmlsoftware.entity.listener;

import java.util.Date;

import com.gmlsoftware.entity.Client;
import com.gmlsoftware.service.UseLogger;
import com.gmlsoftware.util.ObjectMapperUtil;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class ClientListener
	extends GenericListener
	implements UseLogger {

	protected ClientListener(ObjectMapperUtil _objectMapper) {
		super(_objectMapper, ClientListener.class);
	}

	@Override
	@PrePersist
	public void beforeCreate(Object entity) {
		Client client = (Client) entity;
		client.setCreationDate(new Date());
		super.beforeCreate(client);
	}

	@Override
	@PreUpdate
	public void beforeUpdate(Object entity) {
		Client client = (Client) entity;
		client.setModificationDate(new Date());
		super.beforeUpdate(client);
	}
}
