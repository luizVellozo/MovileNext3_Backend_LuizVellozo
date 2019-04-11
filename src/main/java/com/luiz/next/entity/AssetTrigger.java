package com.luiz.next.entity;

public interface AssetTrigger {
	
	public void trigger(final Asset asset);
	
	public boolean isChange();
	
	public boolean isTrigger();
	
	public void check(final Asset asset, final Object evalValue);
}
