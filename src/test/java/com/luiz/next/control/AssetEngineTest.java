package com.luiz.next.control;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.luiz.next.entity.Asset;

public class AssetEngineTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		Asset a = new Asset.Builder("a").byValue(10).build();
		Asset b = new Asset.Builder("b").byValue(20).build();
				
		Asset plus = new Asset.Builder("plus",0,"a + b").build();
		List<Asset> assetDependencies = Arrays.asList(a,b);
		
		AssetEngine assetEngine = new AssetEngine(new AssetChangeTrigger());
		assetEngine.processAsset(plus, plus.getFormula(), assetDependencies);
		
		assertEquals(Integer.class, plus.getValue().getType());
		assertEquals(30,plus.getValue().get());
	}
	
	@Test
	public void facedeTest() {

		Asset a = new Asset.Builder("a").byValue(10).build();
		Asset b = new Asset.Builder("b").byValue(20).build();
				
		Asset asset = new Asset.Builder("division",0,"(a + b)/2").build();
		asset.loadDependecies(Arrays.asList(a,b));

		AssetEngine assetEngine = new AssetEngine(new AssetChangeTrigger());
		assetEngine.processAsset(asset);
		
		assertEquals(Integer.class, asset.getValue().getType());
		assertEquals(15,asset.getValue().get());
	}
	
	@Test
	public void createTest() {

		HashMap<String, Asset> hashMap = new HashMap<String, Asset>() {
			{
				put("a", new Asset.Builder("a").byValue(13).build());
				put("b", new Asset.Builder("b").byValue(26).build());
			}
		};

		Asset asset = new Asset.Builder("division", 0.0, "(a + b)/2").build();
		for (String depedency : asset.getFormula().loadDependencies()) {
			asset.addDependencie(hashMap.get(depedency));
		}

		AssetEngine assetEngine = new AssetEngine(new AssetChangeTrigger());
		assetEngine.processAsset(asset);
		
		assertEquals(Double.class, asset.getValue().getType());
		assertEquals(19.5, asset.getValue().get());
	}
	
	@Test
	@Ignore
	public void testBitSetTypeTest() {
		
		//BitsValue bitsValue = new BitsValue(new byte[] {0,1,0,0});

		/*
		 * Asset asset = new Asset.Builder("bitValue").withValue(bitsValue)
		 * .withFormula("if(bitValue.get(1) == 1) print('ENTROU'); var r = Java.type('byte[]'); var r2 = new r(4); r2[0] = 0;r2[1] = 1;r2[2] = 0;r2[3] = 0; print(r2); return r2;"
		 * ).build();
		 * 
		 * 
		 * Set<String> loadAsset = asset.getFormula().loadAsset();
		 * System.out.println(loadAsset);
		 * 
		 * AssetEngine assetEngine = new AssetEngine(); AssetResult result =
		 * assetEngine.processAsset(asset);
		 * 
		 * System.out.println(result.getErrors());
		 * System.out.println(result.getAsset()); System.out.println(result.getValue());
		 * assertEquals(true,result.isSucess()); //assertEquals(13,result.getValue());
		 * assertEquals(true,result.isChange());
		 */
		
	}

}
