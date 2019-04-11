package com.luiz.next.control;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.luiz.next.entity.Asset;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class AssetReporitoryTest {
	
	@Autowired
	private AssetReporitory repository;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Asset a = new Asset.Builder("a").byValue(10).build();
		Asset b = new Asset.Builder("b").byValue(20).build();
				
		Asset division = new Asset.Builder("division",0,"(a + b)/2").build();
		division.loadDependecies(Arrays.asList(a,b));
		
		Asset plus = new Asset.Builder("plus",0,"a + b").build();
		plus.loadDependecies((Arrays.asList(a,b)));
		
		AssetEngine assetEngine = new AssetEngine(new AssetChangeTrigger());
		assetEngine.processAsset(plus);
		assetEngine.processAsset(division);
		
		repository.save(a);
		repository.save(b);
		repository.save(plus);
		repository.save(division);
		
		assertEquals(10, a.getValue().get());
		
		List<Asset> findAll = repository.findAllFetch();
		for (Asset asset3 : findAll) {
			System.out.println(asset3.getValue().get());
			System.out.println(asset3.getValue().getClass());
		}
	}

}
