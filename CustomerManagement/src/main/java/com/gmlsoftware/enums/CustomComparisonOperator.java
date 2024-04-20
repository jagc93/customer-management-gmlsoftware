package com.gmlsoftware.enums;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;

public class CustomComparisonOperator {
	public static final ComparisonOperator LIKE = new ComparisonOperator("=like=", true);
    public static final ComparisonOperator ILIKE = new ComparisonOperator("=ilike=", true);
}
