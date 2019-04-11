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
public class IntValue extends ValueEntity<Integer> {
	
	private Integer intValue;
	

	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name = "integer_history",joinColumns = @JoinColumn(name = "id"))
	private List<HistoryValue<Integer>> history;
	
	public IntValue() {
		
	}
	
	public IntValue(Integer value) {
		this.intValue = value;
	}

	@Override
	public Integer get() {
		return intValue;
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

	@Override
	public void setTypedValue(Integer value) {
		this.intValue = value;
	}

	@Override
	public void setValue(Object value) {
		setTypedValue((Integer)value);
	}

	@Override
	public List<HistoryValue<Integer>> getHistory() {
		if(history == null)
			this.history = new ArrayList<HistoryValue<Integer>>();
		return history;
	}
}
