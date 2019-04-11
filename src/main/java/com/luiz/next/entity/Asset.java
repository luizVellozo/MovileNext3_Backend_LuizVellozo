package com.luiz.next.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.luiz.next.entity.value.ValueEntity;
import com.sun.istack.NotNull;

@Entity
public class Asset {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asset_seg")
	@SequenceGenerator(name = "asset_seg", sequenceName = "asset_seg", allocationSize = 1, initialValue = 1)
	private long id;
	
	@NotNull
	@Column(nullable=false,unique=true)
	private String name;
	
	@NotNull
	@Embedded
	private Formula formula;
	
	@NotNull
	@OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL,optional=false)
	private ValueEntity<?> value;
	
	@ManyToMany(fetch=FetchType.LAZY)
	private Set<Asset> dependencies;
	
	public Asset() {
		
	}
	
	public Asset(String name,ValueEntity<?> type) {
		this.name = name;
		this.value = type;
	}

	public Asset(String name,ValueEntity<?> type,Formula formula) {
		this(name,type);
		this.formula = formula;
	}
	
	//TODO dynamic fixed attributes
	
	
	public boolean hasFormula() {
		return formula != null;
	}
	
	public void defineFormula(final Formula formula) {
		this.formula = formula;
	} 
	
	public boolean hasDependencies() {
		return dependencies != null && !dependencies.isEmpty(); 
	}
	
	public void loadDependecies(final Collection<Asset> dependencies) {
		if(this.dependencies == null)
			this.dependencies = new HashSet<>();
		this.dependencies.addAll(dependencies);
	}
	
	public void addDependencie(Asset asset) {
		if(this.dependencies == null)
			this.dependencies = new HashSet<>();
		this.dependencies.add(asset);
	}
	
	public Set<Asset> getDependencies() {
		return Collections.unmodifiableSet(dependencies);
	}

	public String getName() {
		return name;
	}

	public Formula getFormula() {
		return formula;
	}

	public ValueEntity<?> getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value.setValue(value);
	}
	
	@Override
	public String toString() {
		return "Asset [" + name + ", " + value + "]";
	}


	public static final class Builder {

		private String name;
		private ValueEntity<? extends Object> value;
		private Formula formula;
		
		public Builder(String name) {
			this.name = name;
		}
		public Builder(String name,Object value,String formula) {
			this.name = name;
			this.byValue(value);
			this.withFormula(formula);
		}
		public Asset build() {
			if(this.formula == null)
				return new Asset(name,value);
			return new Asset(name,value,formula);
		}
		public Builder withFormula(String formula){
			this.formula = new Formula(formula);
			return this;
		}
		public Builder withFormula(Formula formula){
			this.formula = formula;
			return this;
		}
		public Builder withValue(ValueEntity<? extends Object> value) {
			this.value = value;
			return this;
		}
		public Builder byValue(Object value) {
			if(value == null)
				throw new IllegalArgumentException();
			
			if(value instanceof Integer) {
				this.value = new com.luiz.next.entity.value.IntValue();
				this.value.setValue(value);
				return this;
			}
			if(value instanceof Long) {
				this.value = new com.luiz.next.entity.value.LongValue();
				this.value.setValue(value);
				return this;
			}
			if(value instanceof String) {
				this.value = new com.luiz.next.entity.value.StringValue();
				this.value.setValue(value);
				return this;
			}
			if(value instanceof Double) {
				this.value = new com.luiz.next.entity.value.DoubleValue();
				this.value.setValue(value);
				return this;
			}
			if(value instanceof LocalDateTime) {
				this.value = new com.luiz.next.entity.value.LocalDateTimeValue();
				this.value.setValue(value);
				return this;
			}
			return this;
		}
	}
}
