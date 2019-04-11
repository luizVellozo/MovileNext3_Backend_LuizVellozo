package com.luiz.next.entity;

public class SimpleAssetTrigger implements AssetTrigger {

	private boolean change = false;
	
	@Override
	public void trigger(final Asset asset) {
		System.out.println("Trigger: "+asset);
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
