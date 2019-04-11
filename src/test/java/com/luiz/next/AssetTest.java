package com.luiz.next;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.luiz.next.entity.Asset;

public class AssetTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAssetType() {

		Asset asset = new Asset.Builder("Teste").byValue(10).build();
		
		Assert.assertTrue(asset.getValue().get() instanceof Integer);
		Assert.assertEquals(10, asset.getValue().get());
		
	}

}
