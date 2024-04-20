package com.gmlsoftware.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.gmlsoftware.controller.base.BaseControllerTest;
import com.gmlsoftware.factory.ClientFactory;
import com.gmlsoftware.model.client.ClientDto;
import com.gmlsoftware.model.client.ClientRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest
	extends BaseControllerTest<ClientRequest, ClientDto, ClientFactory, Long> {

	protected ClientControllerTest() {
		super("client", faker.random().nextLong());
	}
}
