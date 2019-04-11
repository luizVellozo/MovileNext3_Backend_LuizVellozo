package com.luiz.next.entity.value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

import com.luiz.next.entity.HistoryValue;

@Entity
public class LocalDateTimeValue extends ValueEntity<LocalDateTime> {
	
	private LocalDateTime dateValue;
	
	@CollectionTable(name = "localdatetime_history",joinColumns = @JoinColumn(name = "id"))
	@ElementCollection(fetch=FetchType.LAZY)
	private List<HistoryValue<LocalDateTime>> history;
	
	@Override
	public LocalDateTime get() {
		return dateValue;
	}

	@Override
	public void setValue(final Object value) {
		this.dateValue = (LocalDateTime) value;
	}

	@Override
	public Class<LocalDateTime> getType() {
		return LocalDateTime.class;
	}

	@Override
	public void setTypedValue(LocalDateTime value) {
		this.dateValue = value;
		
	}
	
	@Override
	public List<HistoryValue<LocalDateTime>> getHistory() {
		if(history == null)
			this.history = new ArrayList<HistoryValue<LocalDateTime>>();
		return history;
	}
	

}
