package com.luiz.next.boundary;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiz.next.control.AssetChangeTrigger;
import com.luiz.next.control.AssetEngine;
import com.luiz.next.control.AssetReporitory;
import com.luiz.next.entity.Asset;
import com.luiz.next.entity.AssetResult;
import com.luiz.next.entity.Formula;

@Service
public class AssetService {
	
	@Autowired
	private AssetReporitory repository;
	
	@Autowired
	private AssetChangeTrigger trigger;
	
	public AssetResult CreateNewAssetWithFormula(final String assetName, final Object value, final String exp) {
		
		if(assetName == null || assetName.isEmpty())
			return new AssetResult("Name is not valid");
		
		if(value == null)
			return new AssetResult("Value is not valid");
		
		Formula formula = new Formula(exp);
		
		if(!formula.isValid())
			return new AssetResult("Not Valid Formula");
		
		// TODO compile formula before try get dependecies
		
		final Set<String> dependenciesNames = formula.loadDependencies();
		dependenciesNames.remove(assetName);
		List<Asset> dependencies = null;
		if(!dependenciesNames.isEmpty()) {
			dependencies = repository.loadAllDependenciesIn(assetName,dependenciesNames);
			final Map<String, Asset> assetsByName = dependencies.stream().collect(Collectors.toMap(Asset::getName, asset -> asset));
			
			if(dependencies.isEmpty()) return new AssetResult("Asset already exists");
			
			for (String name : dependenciesNames) {
				if(!assetsByName.containsKey(name))
					return new AssetResult("Not find dependency: "+name); 
			}
		} 

		Asset newAsset = new Asset.Builder(assetName).withFormula(formula).byValue(value).build();
		if(dependencies != null)
			newAsset.loadDependecies(dependencies);
		
		AssetEngine assetEngine = new AssetEngine(trigger);
		AssetResult result = assetEngine.processAsset(newAsset);
		
		if(result.isSucess()) {
			repository.save(newAsset);
		}
		
		return result;
	}
	
	public AssetResult CreateNewAsset(String name, Object value) {
		
		if(name == null || name.isEmpty())
			return new AssetResult("Name is not valid");
		
		if(value == null)
			return new AssetResult("Value is not valid");
		
		if(repository.exists(name)) return new AssetResult("Asset already exists");
		
		Asset newAsset = new Asset.Builder(name).byValue(value).build();
		
		repository.save(newAsset);
		
		return new AssetResult(true, true, newAsset);
	}
	
	public AssetResult processAsset(String assetName) {
		
		if(assetName == null || assetName.isEmpty())
			return new AssetResult("Name is not valid");
		
		Optional<Asset> find = repository.findByNameWithDependencies(assetName);
		if(!find.isPresent()) {
			return new AssetResult("Not find asset: "+assetName);
		}
		
		AssetEngine assetEngine = new AssetEngine(trigger);		
		return assetEngine.processAsset(find.get());
	}
	
	@Transactional
	public AssetResult updateValueIn(String assetName, Object value) {
		final AssetResult processAsset = this.processAsset(assetName, value);
		if(processAsset.isSucess())
			repository.saveAndFlush(processAsset.getAsset());
		return processAsset;
	}
	
	public AssetResult processAsset(String assetName, Object value) {
		
		if(assetName == null || assetName.isEmpty())
			return new AssetResult("Name is not valid");
		
		final Optional<Asset> find = repository.findByNameWithDependencies(assetName);
		if(!find.isPresent()) {
			return new AssetResult("Not find asset: "+assetName);
		}
		final Asset asset = find.get();
		asset.setValue(value);
		AssetEngine assetEngine = new AssetEngine(trigger);		
		return assetEngine.processAsset(asset);
	}
	
	public AssetResult getAsset(String assetName) {
		
		if(assetName == null || assetName.isEmpty())
			return new AssetResult("Name is not valid"); 
		
		Optional<Asset> find = repository.findByName(assetName);
		if(!find.isPresent()) {
			return new AssetResult("Not find asset: "+assetName);
		}
				
		return new AssetResult(true,false,find.get());
	}
}
