package com.luiz.next.control;

import java.util.Collection;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luiz.next.entity.Asset;
import com.luiz.next.entity.AssetResult;
import com.luiz.next.entity.AssetTrigger;
import com.luiz.next.entity.Formula;

public final class AssetEngine {
	
	private final ScriptEngine engine;
	private final static String engineName = "nashorn";
	private final AssetTrigger trigger;
	
	private final static Logger log = LoggerFactory.getLogger(AssetEngine.class);
	
	public AssetEngine(final AssetTrigger trigger) {
		//TODO check cost of create engine
		engine = new ScriptEngineManager().getEngineByName(engineName);
		this.trigger = trigger;
	}
	
	public AssetResult processAsset(Asset asset, Formula formula, Collection<Asset> assetDependencies) {
		
		engine.put(asset.getName(), asset.getValue().get());
		loadDependencies(assetDependencies);
		return process(asset, formula);
	}

	private void loadDependencies(final Collection<Asset> assetDependencies) {
		for (Asset dep : assetDependencies) {
			engine.put(dep.getName(), dep.getValue().get());
		}
	}
	
	public AssetResult processAsset(Asset asset) {
		
		engine.put(asset.getName(), asset.getValue().get());
		if(asset.hasDependencies()) {
			loadDependencies(asset.getDependencies());
		}
		if(asset.hasFormula()) {
			return process(asset, asset.getFormula());
		}
		return new AssetResult(true,false,asset);
	}
	
	private AssetResult process(Asset asset, Formula formula) {
		try {
			final Object eval = engine.eval(formula.buildCode(asset));
			trigger.check(asset, eval);
			return new AssetResult(true, trigger.isChange(), asset);
		} catch (Exception e) {
			log.error("Erro in EVAL Asset: {} error: {}", asset.getName(),e.getMessage());
			return new AssetResult(e);
		}
	}

}
