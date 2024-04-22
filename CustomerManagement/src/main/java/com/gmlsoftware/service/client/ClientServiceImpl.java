package com.gmlsoftware.service.client;

import static com.gmlsoftware.config.logger.LoggerConfig.logger;
import static com.gmlsoftware.config.logger.LoggerConfig.error;
import static com.gmlsoftware.config.logger.LoggerConfig.info;
import static com.gmlsoftware.constant.ClientConstant.MAX_EMAIL_ADDRESS_LENGTH;
import static com.gmlsoftware.constant.ClientConstant.MAX_FIRST_NAME_LENGTH;
import static com.gmlsoftware.constant.ClientConstant.MAX_LAST_NAME_LENGTH;
import static com.gmlsoftware.constant.ClientConstant.MAX_MIDDLE_NAME_LENGTH;
import static com.gmlsoftware.constant.ClientConstant.MAX_PHONE_NUMBER_LENGTH;
import static com.gmlsoftware.constant.ClientConstant.MAX_SECOND_LAST_NAME_LENGTH;
import static com.gmlsoftware.constant.GlobalConstant.LOG_ALREADY_EXIST;
import static com.gmlsoftware.constant.GlobalConstant.LOG_CREATE_INIT;
import static com.gmlsoftware.constant.GlobalConstant.LOG_UPDATE_INIT;
import static com.gmlsoftware.exception.GenericException.getNotFound;
import static com.gmlsoftware.exception.GenericException.requireNotEmpty;
import static com.gmlsoftware.exception.GenericException.validateFieldLength;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gmlsoftware.entity.Client;
import com.gmlsoftware.model.client.ClientDto;
import com.gmlsoftware.model.client.ClientMapper;
import com.gmlsoftware.model.client.ClientRequest;
import com.gmlsoftware.repository.ClientRepository;
import com.gmlsoftware.service.GenericServiceImpl;
import com.gmlsoftware.service.UseLogger;
import com.gmlsoftware.util.ObjectMapperUtil;

@Service
public class ClientServiceImpl
	extends GenericServiceImpl<ClientRequest, Client, ClientDto, Long>
	implements ClientService, UseLogger {

	private static final String TABLE_NAME = "Client";
	private static final String FIELD_FIRST_NAME = "firstName";
	private static final String FIELD_MIDDLE_NAME = "middleName";
	private static final String FIELD_LAST_NAME = "lastName";
	private static final String FIELD_SECOND_LAST_NAME = "secondLastName";
	private static final String FIELD_EMAIL_ADDRESS = "emailAddress";
	private static final String FIELD_PHONE_NUMBER = "phoneNumber";

	private final ClientRepository repository;
	private final ObjectMapperUtil objectMapper;
	private final ClientMapper mapper;
	private final Logger log;

	public ClientServiceImpl(
			ClientRepository _repository,
			ClientMapper _mapper,
			ObjectMapperUtil _objectMapper
	) {
		super(_repository, _repository, _mapper);
		this.repository = _repository;
		this.objectMapper = _objectMapper;
		this.mapper = _mapper;
		this.log = logger(this.getClass());
	}

	@Override
	public ClientDto create(ClientRequest request) {
		info(log, String.format(LOG_CREATE_INIT, objectMapper.writeValueAsString(request)));
		validateRequiredFields(request);
		validateFieldsLength(request);
		validateFieldsUnique(request, null);

		Client client = mapper.toEntity(request);
		client.setSharedKey(getSharedKey(client));

		return super.createByEntity(client);
	}

	@Override
	public ClientDto update(Long id, ClientRequest request) {
		info(log, String.format(LOG_UPDATE_INIT, objectMapper.writeValueAsString(request)));
		validateFieldsLength(request);
		validateFieldsUnique(request, id);

		Client client = super.findById(id).orElseThrow(() -> getNotFound(id, TABLE_NAME));
		mapper.updateFromRequest(request, client);
		client.setSharedKey(getSharedKey(client));

		return super.update(client);
	}

	private String getSharedKey(Client client) {
		return String.format("%s%s", client.getFirstName().charAt(0), client.getLastName()).toLowerCase();
	}

	private void validateRequiredFields(ClientRequest request) {
		try {
			requireNotEmpty(request.getFirstName(), FIELD_FIRST_NAME);
			requireNotEmpty(request.getLastName(), FIELD_LAST_NAME);
			requireNotEmpty(request.getEmailAddress(), FIELD_EMAIL_ADDRESS);
		} catch (NullPointerException | IllegalArgumentException e) {
			error(log, e.getMessage());
			throw e;
		}
	}

	private void validateFieldsLength(ClientRequest request) {
		try {
			validateFieldLength(request.getFirstName(), MAX_FIRST_NAME_LENGTH, FIELD_FIRST_NAME);
			validateFieldLength(request.getMiddleName(), MAX_MIDDLE_NAME_LENGTH, FIELD_MIDDLE_NAME);
			validateFieldLength(request.getLastName(), MAX_LAST_NAME_LENGTH, FIELD_LAST_NAME);
			validateFieldLength(request.getSecondLastName(), MAX_SECOND_LAST_NAME_LENGTH, FIELD_SECOND_LAST_NAME);
			validateFieldLength(request.getEmailAddress(), MAX_EMAIL_ADDRESS_LENGTH, FIELD_EMAIL_ADDRESS);
			validateFieldLength(request.getPhoneNumber(), MAX_PHONE_NUMBER_LENGTH, FIELD_PHONE_NUMBER);
		} catch (NullPointerException | IllegalArgumentException e) {
			error(log, e.getMessage());
			throw e;
		}
	}

	private void validateFieldsUnique(ClientRequest request, Long id) {
		boolean existBy = validateEmailAddressUnique(request.getEmailAddress(), id);

		if (existBy) {
			throw new IllegalArgumentException(
					String.format(LOG_ALREADY_EXIST, TABLE_NAME, FIELD_EMAIL_ADDRESS, request.getEmailAddress())
			);
		}
	}

	private boolean validateEmailAddressUnique(String emailAddress, Long id) {
		if (id != null) {
			return repository.existsByEmailAddressIgnoreCaseAndClientIDNot(emailAddress, id);
		}

		return repository.existsByEmailAddressIgnoreCase(emailAddress);
	}
}
