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
public class LongValue extends ValueEntity<Long> {
	
	private Long longValue;
	
	@CollectionTable(name = "long_history",joinColumns = @JoinColumn(name = "id"))
	@ElementCollection(fetch=FetchType.LAZY)
	private List<HistoryValue<Long>> history;
	
	public LongValue() {
		
	}
	
	public LongValue(Long value) {
		this.longValue = value;
	}

	@Override
	public Long get() {
		return longValue;
	}

	@Override
	public Class<Long> getType() {
		return Long.class;
	}

	@Override
	public void setTypedValue(Long value) {
		this.longValue = value;
	}
	
	@Override
	public List<HistoryValue<Long>> getHistory() {
		if(history == null)
			this.history = new ArrayList<HistoryValue<Long>>();
		return history;
	}

	@Override
	public void setValue(Object value) {
		setValue((Long)value);
	}
}
