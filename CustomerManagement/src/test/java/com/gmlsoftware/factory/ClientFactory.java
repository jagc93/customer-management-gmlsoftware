package com.gmlsoftware.factory;

import static com.gmlsoftware.constant.ClientConstant.*;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.gmlsoftware.factory.base.BaseFactory;
import com.gmlsoftware.factory.base.BaseFactoryImpl;
import com.gmlsoftware.model.client.ClientDto;
import com.gmlsoftware.model.client.ClientRequest;
import com.gmlsoftware.service.client.ClientService;

@Component
public class ClientFactory
	extends BaseFactoryImpl<ClientRequest, ClientDto, Long, ClientService>
	implements BaseFactory<ClientRequest, ClientDto> {

	protected ClientFactory(ClientService _service) {
		super(_service);
	}

	@Override
	public ClientDto create() {
		return super.create(request());
	}

	public ClientDto create(ClientRequest request) {
		return super.create(request);
	}

	@Override
	public ClientRequest request() {
		return new ClientRequest(
				faker.lorem().characters(MAX_FIRST_NAME_LENGTH),
				faker.lorem().characters(MAX_MIDDLE_NAME_LENGTH),
				faker.lorem().characters(MAX_LAST_NAME_LENGTH),
				faker.lorem().characters(MAX_SECOND_LAST_NAME_LENGTH),
				faker.lorem().characters(MAX_EMAIL_ADDRESS_LENGTH),
				faker.lorem().characters(MAX_PHONE_NUMBER_LENGTH),
				new Date(),
				new Date()
		);
	}
}
