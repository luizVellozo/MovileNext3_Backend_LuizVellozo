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
public class BooleanValue extends ValueEntity<Boolean> {
	
	private Boolean boolValue;
	
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name = "boolean_history",joinColumns = @JoinColumn(name = "id"))
	private List<HistoryValue<Boolean>> history;
	
	public BooleanValue() {
		
	}
	
	public BooleanValue(Boolean value) {
		this.boolValue = value;
	}

	@Override
	public Boolean get() {
		return boolValue;
	}

	@Override
	public Class<Boolean> getType() {
		return Boolean.class;
	}

	@Override
	public void setTypedValue(Boolean value) {
		this.boolValue = value;
	}

	@Override
	public void setValue(Object value) {
		setTypedValue((Boolean)value);
	}
	
	@Override
	public List<HistoryValue<Boolean>> getHistory() {
		if(history == null)
			this.history = new ArrayList<HistoryValue<Boolean>>();
		return history;
	}
}
