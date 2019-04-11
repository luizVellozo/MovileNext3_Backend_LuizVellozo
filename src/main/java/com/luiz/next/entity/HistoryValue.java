package com.luiz.next.entity;

import java.time.LocalDateTime;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Embeddable
public class HistoryValue<T extends Object> {

	protected T value;
	
	protected LocalDateTime addedDate;
	
	public HistoryValue() {
		
	}

	public HistoryValue(T value) {
		this.value = value;
		this.addedDate = LocalDateTime.now();
	}

	public T getValue() {
		return value;
	}

	public LocalDateTime getAddedDate() {
		return addedDate;
	}
	
	
	@Override
	public String toString() {
		return addedDate+" : " + value;
	}
	
	
}
