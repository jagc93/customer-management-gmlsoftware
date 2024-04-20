package com.gmlsoftware.model;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

/**
 * @param <R> Request
 * @param <E> Entity
 * @param <D> Dto
 */
public interface GenericMapper<R, E, D> {

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateFromRequest(R request, @MappingTarget E entity);

	E toEntity(R request);

	D toDto(E entity);

	List<D> toListDto(List<E> entities);

	default Page<D> toPageDto(Page<E> page) {
		return page.map(this::toDto);
	}
}
