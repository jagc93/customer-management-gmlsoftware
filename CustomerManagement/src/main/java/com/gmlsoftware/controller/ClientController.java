package com.gmlsoftware.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmlsoftware.model.client.ClientDto;
import com.gmlsoftware.model.client.ClientRequest;
import com.gmlsoftware.service.client.ClientService;

@RestController
@RequestMapping("client")
public class ClientController
	extends GenericController<ClientDto, ClientRequest, Long> {

	public ClientController(ClientService _service) {
		super(_service);
	}
}
