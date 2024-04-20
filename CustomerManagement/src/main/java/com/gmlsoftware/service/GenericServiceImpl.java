package com.gmlsoftware.service;

import static com.gmlsoftware.exception.GenericException.getNotFound;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.gmlsoftware.model.GenericMapper;
import com.gmlsoftware.rsql.CustomRsqlVisitor;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

/**
 * 
 * @param <R>  Request
 * @param <E>  Entity
 * @param <D>  Dto
 * @param <ID> Id type in entity
 */
public class GenericServiceImpl<R, E, D, ID> {

	private final JpaSpecificationExecutor<E> jpaSpecificationExecutor;
	private final JpaRepository<E, ID> jpaRepository;
	private final GenericMapper<R, E, D> mapper;
	private final Class<E> entityClass;

	public GenericServiceImpl(
		JpaSpecificationExecutor<E> _jpaSpecificationExecutor,
		JpaRepository<E, ID> _jpaRepository,
		GenericMapper<R, E, D> _mapper
	) {
		this.jpaSpecificationExecutor = _jpaSpecificationExecutor;
		this.jpaRepository = _jpaRepository;
		this.mapper = _mapper;
		this.entityClass = extractEntityClass();
	}

	@Transactional(readOnly = true)
	public Page<D> index(Pageable pageable, String search) {

		if (search == null || search.isBlank()) {
			return jpaRepository.findAll(pageable)
					.map(mapper::toDto);
		}

		Node rootNode = new RSQLParser().parse(search);
	    Specification<E> spec = rootNode.accept(new CustomRsqlVisitor<E>());

		return jpaSpecificationExecutor.findAll(spec, pageable)
				.map(mapper::toDto);
	}

	@Transactional(readOnly = true)
	public D show(ID id) {
		return findById(id)
				.map(mapper::toDto)
				.orElseThrow(() -> getNotFound(id, getEntitySimpleName()));
	}

	@Transactional
	public D create(R request) {
		return Optional.of(request)
				.map(mapper::toEntity)
				.map(this::createByEntity)
				.get();
	}

	@Transactional
	public D createByEntity(E entity) {
		return Optional.of(entity)
				.map(jpaRepository::save)
				.map(mapper::toDto)
				.get();
	}

	@Transactional
	public D update(ID id, R request) {
		E entity = findById(id)
				.orElseThrow(() -> getNotFound(id, getEntitySimpleName()));

		mapper.updateFromRequest(request, entity);

		return update(entity);
	}

	@Transactional
	public D update(E entity) {
		return mapper.toDto(jpaRepository.save(entity));
	}

	@Transactional
	public void delete(ID id) {
		E entity = findById(id)
				.orElseThrow(() -> getNotFound(id, getEntitySimpleName()));

		jpaRepository.delete(entity);
	}

	@Transactional(readOnly = true)
	protected Optional<E> findById(ID id) {
		return jpaRepository.findById(id);
	}

	@SuppressWarnings("unchecked")
	private Class<E> extractEntityClass() {
		Type genericSuperclass = getClass().getGenericSuperclass();

		if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();

            if (actualTypeArguments.length >= 2 && actualTypeArguments[1] instanceof Class) {
                return (Class<E>) actualTypeArguments[1];
            } else {
                throw new IllegalStateException("Cannot determine entity class");
            }
        } else {
            throw new IllegalStateException("Cannot determine entity class");
        }
	}

	private String getEntitySimpleName() {
        return entityClass.getSimpleName();
    }
}
