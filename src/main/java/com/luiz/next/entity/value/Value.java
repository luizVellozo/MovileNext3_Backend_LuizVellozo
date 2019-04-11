package com.luiz.next.entity.value;

import java.util.List;

import com.luiz.next.entity.HistoryValue;


public interface Value<T extends Object> {
	
	T get();
	
	void setTypedValue(T value);
	
	Class<T> getType();
	
	void setValue(final Object value);
	
	String getTypeName();
	
	boolean isNull();
	
	List<HistoryValue<T>> getHistory();
	
}
