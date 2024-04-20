package com.gmlsoftware.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
 * @param <D>  Dto
 * @param <R>  Request
 * @param <ID> Id type in entity
 */
public interface GenericService<D, R, ID> {
	public Page<D> index(Pageable pageable, String search);
	public D show(ID id);
	public D create(R request);
	public D update(ID id, R request);
	public void delete(ID id);
}
