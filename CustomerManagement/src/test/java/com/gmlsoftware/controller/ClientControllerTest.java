package com.gmlsoftware.controller;

import static com.gmlsoftware.constant.ClientConstant.MAX_FIRST_NAME_LENGTH;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.gmlsoftware.controller.base.BaseControllerTest;
import com.gmlsoftware.factory.ClientFactory;
import com.gmlsoftware.model.client.ClientDto;
import com.gmlsoftware.model.client.ClientRequest;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest
	extends BaseControllerTest<ClientRequest, ClientDto, ClientFactory, Long> {

	protected ClientControllerTest() {
		super("client", faker.random().nextLong());
	}

	@Test
	@Transactional
	void show() throws Exception {
		ClientDto dto = factory.create();

		String content = mockMvc.perform(
				MockMvcRequestBuilders.get(uriId, dto.getClientID())
                .contentType(MediaType.APPLICATION_JSON)
        )
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn()
		.getResponse()
		.getContentAsString();

		ClientDto result = objectMapper.readValue(content, ClientDto.class);
		Assertions.assertEquals(dto.getClientID(), result.getClientID());
		Assertions.assertEquals(dto.getEmailAddress(), result.getEmailAddress());
		Assertions.assertEquals(dto.getFirstName(), result.getFirstName());
		Assertions.assertEquals(dto.getLastName(), result.getLastName());
		Assertions.assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
		Assertions.assertEquals(dto.getSharedKey(), result.getSharedKey());
		Assertions.assertNotNull(result.getCreationDate());

		LocalDate expected = dateToLocalDate(dto.getCreationDate());
		LocalDate actual = dateToLocalDate(result.getCreationDate());

		Assertions.assertEquals(expected, actual);
	}

	@Test
	@Transactional
	void create() throws Exception {
		ClientRequest request = factory.request();

		String content = mockMvc.perform(
				MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andReturn()
			.getResponse()
			.getContentAsString();
		
		ClientDto result = objectMapper.readValue(content, ClientDto.class);
		
		Assertions.assertNotNull(result.getClientID());
		Assertions.assertNotNull(result.getSharedKey());
		Assertions.assertNotNull(result.getLastName());
		Assertions.assertNotNull(result.getFirstName());
		Assertions.assertNotNull(result.getEmailAddress());
		Assertions.assertNotNull(result.getCreationDate());

		Assertions.assertEquals(request.getFirstName(), result.getFirstName());
		Assertions.assertEquals(request.getLastName(), result.getLastName());
		Assertions.assertEquals(request.getEmailAddress(), result.getEmailAddress());
	}

	@Test
	@Transactional
	void update() throws Exception {
		ClientDto dto = factory.create();
		ClientRequest request = new ClientRequest();
		request.setFirstName(faker.lorem().characters(MAX_FIRST_NAME_LENGTH));

		mockMvc.perform(
				MockMvcRequestBuilders.patch(uriId, dto.getClientID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
			.andExpectAll(
					MockMvcResultMatchers.status().isOk(),
					MockMvcResultMatchers.jsonPath("$.firstName", Matchers.equalTo(request.getFirstName())),
					MockMvcResultMatchers.jsonPath("$.lastName", Matchers.equalTo(dto.getLastName())),
					MockMvcResultMatchers.jsonPath("$.emailAddress", Matchers.equalTo(dto.getEmailAddress())),
					MockMvcResultMatchers.jsonPath("$.sharedKey", Matchers.notNullValue())
			);
	}

	private LocalDate dateToLocalDate(Date d) {
		Instant instant = d.toInstant();
		return instant.atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
