package com.gmlsoftware.factory.base;

import com.github.javafaker.Faker;
import com.gmlsoftware.service.GenericService;

/**
 * @param <R> Request
 * @param <D> Dto
 * @param <ID> Id type in entity
 * @param <S> Service
 */
public class BaseFactoryImpl<R, D, ID, S extends GenericService<D, R, ID>> {

	protected static final Faker faker = new Faker();
	private final S service;

	protected BaseFactoryImpl(S _service) {
		this.service = _service;
	}

	protected D create(R request) {
		return service.create(request);
	}
}
