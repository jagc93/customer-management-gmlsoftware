package com.gmlsoftware.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.gmlsoftware.service.GenericService;

/**
 * 
 * @param <D>  Dto
 * @param <R>  Request
 * @param <ID> Id type in entity
 */
public class GenericController<D, R, ID> {

	private final GenericService<D, R, ID> service;

	public GenericController(GenericService<D, R, ID> _service) {
		this.service = _service;
	}

	@GetMapping
	public ResponseEntity<Page<D>> index(
        @RequestParam(defaultValue = "") String search,
        Pageable pageable
	) {
		return ResponseEntity.ok(service.index(pageable, search));
	}

	@GetMapping("/{id}")
    public ResponseEntity<D> show(@PathVariable ID id) {
        return ResponseEntity.ok(service.show(id));
    }

	@PostMapping
    public ResponseEntity<D> create(@RequestBody R request) {
        return ResponseEntity
        	.status(HttpStatus.CREATED)
        	.body(service.create(request));
    }

	@PatchMapping("/{id}")
    public ResponseEntity<D> update(@PathVariable ID id, @RequestBody R request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
    	service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
