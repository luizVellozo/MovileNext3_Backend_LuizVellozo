package com.luiz.next.entity.value;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

import com.luiz.next.entity.HistoryValue;

@Entity
public class StringValue extends ValueEntity<String> {
	
	private String stringValue;
	
	@CollectionTable(name = "string_history",joinColumns = @JoinColumn(name = "id"))
	@ElementCollection(fetch=FetchType.LAZY)
	private List<HistoryValue<String>> history;
	
	public StringValue() {
		
	}
	
	public StringValue(String value) {
		this.stringValue = value;
	}

	@Override
	public String get() {
		return stringValue;
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	public void setTypedValue(String value) {
		this.stringValue = value;
	}

	@Override
	public void setValue(Object value) {
		setTypedValue((String)value);
	}
	
	@Override
	public List<HistoryValue<String>> getHistory() {
		if(history == null)
			this.history = new ArrayList<HistoryValue<String>>();
		return history;
	}
}
