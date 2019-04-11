package com.luiz.next.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiz.next.entity.Asset;
import com.luiz.next.entity.AssetTrigger;

@Service
public class AssetChangeTrigger implements AssetTrigger {
	
	private boolean change = false;
	
	@Autowired
	private ValueRepository valueRepository;
	
	@Override
	public void trigger(final Asset asset) {
		System.out.println("Trigger: "+asset);
		if(!asset.getValue().isNew())
			valueRepository.incrementHistory(asset.getValue());
	}

	@Override
	public boolean isChange() {
		return change;
	}

	@Override
	public boolean isTrigger() {
		return change;
	}

	@Override
	public void check(Asset beforeValue, Object newValue) {
		this.change = !beforeValue.getValue().equals(newValue);
		beforeValue.setValue(newValue);
		if(this.change) {
			trigger(beforeValue);
		}
	}

}
