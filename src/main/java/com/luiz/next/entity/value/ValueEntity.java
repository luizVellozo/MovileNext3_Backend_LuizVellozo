package com.luiz.next.entity.value;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;

import com.luiz.next.entity.HistoryValue;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class ValueEntity<T extends Object> implements Value<T> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "value_seg")
	@SequenceGenerator(name = "value_seg", sequenceName = "value_seg", allocationSize = 1, initialValue = 1)
	private long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public boolean isNew() {
		return id <= 0;
	}
	
	@ElementCollection(fetch=FetchType.LAZY)
	public abstract List<HistoryValue<T>> getHistory();
	
	public void incrementHistory() {
		getHistory().add(new HistoryValue<T>(get()));
	}
	
	@Override
	public String getTypeName() {
		return getType().getCanonicalName();
	}
	
	@Override
	public boolean isNull() {
		return get() == null;
	}

	@Override
	public String toString() {
		return "Value: " + get() + ", "+getTypeName();
	}
	
	
}
