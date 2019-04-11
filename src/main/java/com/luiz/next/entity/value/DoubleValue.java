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
public class DoubleValue extends ValueEntity<Double> {
	
	private Double doubleValue;
	
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name = "double_history",joinColumns = @JoinColumn(name = "id"))
	private List<HistoryValue<Double>> history;
	
	public DoubleValue() {
		
	}
	
	public DoubleValue(Double value) {
		this.doubleValue = value;
	}

	@Override
	public Double get() {
		return doubleValue;
	}

	@Override
	public Class<Double> getType() {
		return Double.class;
	}

	@Override
	public void setTypedValue(Double value) {
		this.doubleValue = value;
	}

	@Override
	public void setValue(Object value) {
		setTypedValue((Double)value);
	}

	@Override
	public List<HistoryValue<Double>> getHistory() {
		if(history == null)
			this.history = new ArrayList<HistoryValue<Double>>();
		return history;
	}
}
