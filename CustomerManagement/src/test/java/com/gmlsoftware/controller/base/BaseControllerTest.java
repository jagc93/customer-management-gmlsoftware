package com.gmlsoftware.controller.base;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.github.javafaker.Faker;
import com.gmlsoftware.factory.base.BaseFactory;
import com.gmlsoftware.util.ObjectMapperUtil;

import jakarta.transaction.Transactional;

/**
 * @param <R>  Request
 * @param <D>  Dto
 * @param <F>  Factory
 * @param <ID> Id type in entity
 */
public abstract class BaseControllerTest<R, D, F extends BaseFactory<R, D>, ID> {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapperUtil objectMapper;

	@Autowired
	protected F factory;

	protected static final Faker faker = new Faker();
	protected final String uri;
	protected final String uriId;
	protected final ID id;

	protected BaseControllerTest(String _uri, ID _id) {
		this.uri = String.format("/%s", _uri);
		this.uriId = String.format("%s/{Id}", uri);
		this.id = _id;
	}

	@Test
	@Transactional
    protected void index() throws Exception {
		factory.create();

        mockMvc.perform(
        		MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpectAll(
        		MockMvcResultMatchers.status().isOk(),
        		MockMvcResultMatchers.jsonPath("$.content", Matchers.not(Matchers.hasSize(0)))
        );
    }

	@Test
	protected void showNotFound() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get(uriId, id)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	protected void updateNotExist() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.patch(uriId, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(factory.request()))
        )
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	protected void deleteNotExist() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete(uriId, id)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}
