package com.luiz.next.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Formula {
	
	private String expression;
	
	@Transient
	private final transient static List<String> reservedWords = Arrays.asList("if","else","null","print");
	@Transient
	private final transient static String RETURN = "return";
	
	private Boolean hasReturn = false;
	
	public Formula() {
		
	}

	public Formula(String expression) {
		this.expression = expression;
	}
	
	
	public boolean isHasReturn() {
		return hasReturn;
	}

	public String getExpression() {
		return expression;
	}
	
	public boolean isValid() {
		return expression != null && expression.length() >= 1;
	}
	
	public Set<String> loadDependencies() {
		// TODO define pattern for words - not include numbers and other patterns
		final Pattern pattern = Pattern.compile("\\b[^\\d\\W]+\\b");
		final Matcher matcher = pattern.matcher(expression);
		final Set<String> dependencies = new HashSet<String>();
		
		while(matcher.find()) {
			final String find = expression.substring(matcher.start(), matcher.end());
			dependencies.add(find);
		}
		if(dependencies.contains(RETURN)) {
			this.hasReturn = true;
			dependencies.remove(RETURN);
		}
		// TODO include reservedWords in RegexPattern
		dependencies.removeAll(reservedWords);
		return dependencies;
	}

	public String buildCode(final Asset asset) {
		final StringBuilder exp = new StringBuilder();
		if(isHasReturn()) {
			final String functionName= "wrp_"+asset.getName();
			exp.append("function ").append(functionName).append("(){").append(expression).append("};")
				.append(System.lineSeparator())
				.append("new ")
				.append(asset.getValue().getTypeName())
				.append("(")
				.append(functionName)
				.append("());");
		} else {
			exp.append("new ")
			   .append(asset.getValue().getTypeName())
			   .append("(")
			   .append(expression)
			   .append(")");
		}
		return exp.toString();
	}
}
