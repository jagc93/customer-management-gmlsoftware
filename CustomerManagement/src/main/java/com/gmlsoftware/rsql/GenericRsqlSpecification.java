package com.gmlsoftware.rsql;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.gmlsoftware.enums.RsqlSearchOperation;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GenericRsqlSpecification<T> implements Specification<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String property;
    private ComparisonOperator operator;
    private List<String> arguments;
    private Expression<String> path;
    private boolean isJoin;

    public GenericRsqlSpecification(String property, ComparisonOperator operator, List<String> arguments) {
		super();
		this.property = property;
		this.operator = operator;
		this.arguments = arguments;
	}

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Object> args = castArguments(root);
        Object argument = args.get(0);

        switch (RsqlSearchOperation.getSimpleOperator(operator)) {
	        case EQUAL: {
	            if (argument instanceof String) {
	            	return builder.like(builder.lower(isJoin ? path.as(String.class) : path), argument.toString().toLowerCase().replace('*', '%'));
	            } else if (argument == null) {
	                return builder.isNull(path);
	            } else {
	                return builder.equal(path, argument);
	            }
	        }
	        case NOT_EQUAL: {
	            if (argument instanceof String) {
	                return builder.notLike(builder.lower(isJoin ? path.as(String.class) : path), argument.toString().toLowerCase().replace('*', '%'));
	            } else if (argument == null) {
	                return builder.isNotNull(path);
	            } else {
	                return builder.notEqual(path, argument);
	            }
	        }
	        case GREATER_THAN: {
	            return builder.greaterThan(isJoin ? path.as(String.class) : root.<String> get(property), argument.toString());
	        }
	        case GREATER_THAN_OR_EQUAL: {
	            return builder.greaterThanOrEqualTo(isJoin ? path.as(String.class) : root.<String> get(property), argument.toString());
	        }
	        case LESS_THAN: {
	            return builder.lessThan(isJoin ? path.as(String.class) : root.<String> get(property), argument.toString());
	        }
	        case LESS_THAN_OR_EQUAL: {
	            return builder.lessThanOrEqualTo(isJoin ? path.as(String.class) : root.<String> get(property), argument.toString());
	        }
	        case IN:
	            return path.in(args);
	        case NOT_IN:
	            return builder.not(path.in(args));
        }

        return null;
    }

    private List<Object> castArguments(final Root<T> root) {

    	Class<? extends Object> type;

    	if (property.contains(".")) {
    		String[] properties = property.split("\\.");
    		if (properties.length < 2) {
    			throw new IllegalArgumentException("filter not allowed");
    		}
    		path = getJoin(root, property).get(properties[properties.length - 1]);
    		isJoin = true;
    	} else {
    		path = root.get(property);
    		isJoin = false;
    	}

    	type = path.getJavaType();

        List<Object> args = arguments.stream().map(arg -> {
            if (type.equals(Integer.class)) {
               return Integer.parseInt(arg);
            } else if (type.equals(Long.class)) {
               return Long.parseLong(arg);
            } else if (type.equals(Short.class)) {
                return Short.parseShort(arg);
             } else {
                return arg;
            }
        }).collect(Collectors.toList());

        return args;
    }

    // Método para obtener la relación (join) desde la raíz (root) basándose en el nombre de la propiedad
    // la ultima propiedad se da por entendido que es el campo a filtrar
    private <X, Y> Join<X, Y> getJoin(Root<X> root, String property) {
        String[] properties = property.split("\\.");
        Join<X, Y> join = root.join(properties[0]);
        for (int i = 1; i < properties.length; i++) {
        	if (i < properties.length - 1) {
        		join = join.join(properties[i]);
        	}
        }

        return join;
    }
}
