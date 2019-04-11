package com.luiz.next.entity;

import java.util.ArrayList;
import java.util.List;

public final class AssetResult {
	
	private boolean change = false;
	private Asset asset;
	private boolean sucess = false;
	private List<String> errors;
	private Exception exception;

	public AssetResult(Exception exception) {
		this.exception = exception;
		this.addError(exception.getMessage());
	}
	
	public AssetResult(boolean sucess, boolean change, Asset value) {
		this.sucess = sucess;
		this.change = change;
		this.asset = value;
	}
	
	public AssetResult(String error) {
		addError(error);
	}
	
	public void addError(String error) {
		if(this.errors == null)
			this.errors = new ArrayList<>();
		this.errors.add(error);
		sucess = false;
	}

	public boolean isChange() {
		return change;
	}

	public Asset getAsset() {
		return asset;
	}
	
	public Object getValue() {
		if(asset == null)
			return null;
		return asset.getValue().get();
	}

	public List<String> getErrors() {
		return errors;
	}

	public boolean isSucess() {
		return sucess;
	}

	public Exception getException() {
		return exception;
	}
	
	
	
}
