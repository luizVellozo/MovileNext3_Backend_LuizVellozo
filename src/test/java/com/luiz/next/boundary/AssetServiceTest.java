package com.luiz.next.boundary;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.luiz.next.control.AssetReporitory;
import com.luiz.next.control.ValueRepository;
import com.luiz.next.entity.Asset;
import com.luiz.next.entity.AssetResult;
import com.luiz.next.entity.HistoryValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class AssetServiceTest {

	@Autowired
	private AssetReporitory repository;

	@Autowired
	private AssetService service;
	
	@Autowired
	private ValueRepository valueRepository;

	@Before
	public void setUp() throws Exception {
		repository.deleteAll();

		Asset a = new Asset.Builder("a").byValue(13).build();
		Asset b = new Asset.Builder("b").byValue(26).build();

		repository.save(a);
		repository.save(b);
	}

	@After
	public void clean() throws Exception {

		repository.deleteAll();
	}

	@Test
	public void testCreateNewAsset() {

		AssetResult result = service.CreateNewAssetWithFormula("division", 0.0, "(a + b)/2");
		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(true, result.isSucess());

		assertEquals(true, result.isChange());
	}

	@Test
	public void testErrorNullValue() {

		AssetResult result = service.CreateNewAssetWithFormula("division", null, "(a + b)/2");
		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(false, result.isSucess());
		assertEquals(false, result.isChange());
	}

	@Test
	public void testErrorFormulaWithEmptyDependencies() {

		AssetResult result = service.CreateNewAssetWithFormula("division", 0.0, "(aav + bsd)/2");
		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(false, result.isSucess());

		assertEquals(false, result.isChange());
	}

	@Test
	public void testErrorFormulaWithWrongDependencies() {

		AssetResult result = service.CreateNewAssetWithFormula("division", 0.0, "(a + bda)/2");
		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(false, result.isSucess());

		assertEquals(false, result.isChange());

	}

	@Test
	public void testErrorFormulaNotValidCode() {

		AssetResult result = service.CreateNewAssetWithFormula("division", 0.0, "(a +!* b)/2");
		// System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(false, result.isSucess());

		assertEquals(false, result.isChange());
	}

	@Test
	public void testErrorFormulaNotDependencies() {

		AssetResult result = service.CreateNewAssetWithFormula("test", 0.0, "test + 1");
		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());

		assertEquals(true, result.isSucess());
		assertEquals(1.0, result.getValue());
		assertEquals(true, result.isChange());
	}

	@Test
	public void testErrorFormulaNotDependenciesWithAttrib() {

		AssetResult result = service.CreateNewAssetWithFormula("test", 0.0, "test = 1.0");
		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());

		assertEquals(true, result.isSucess());
		assertEquals(1.0, result.getValue());
		assertEquals(true, result.isChange());
	}

	@Test
	public void testCreateSimpleAsset() {

		AssetResult result = service.CreateNewAsset("A", 10.0);
		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(true, result.isSucess());
		assertEquals(10.0, result.getValue());
		assertEquals(true, result.isChange());

	}

	@Test
	public void testCreateSimpleAssetAndComplex() {

		service.CreateNewAsset("A", 5.5);
		service.CreateNewAsset("B", 10);

		AssetResult resultDivision = service.CreateNewAssetWithFormula("division", 0.0, "(A + B)/2");

		System.out.println(resultDivision.getErrors());
		System.out.println(resultDivision.getAsset());
		assertEquals(true, resultDivision.isSucess());
		assertEquals(7.75, resultDivision.getValue());
		assertEquals(true, resultDivision.isChange());
	}

	@Test
	public void testSimpleProcessAsset() {

		AssetResult result = service.processAsset("a");

		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(true, result.isSucess());
		assertEquals(13, result.getValue());
		assertEquals(false, result.isChange());
	}

	@Test
	public void testSimpleWithFormulaProcessAsset() {

		service.CreateNewAssetWithFormula("test", 10, "test + 1");
		AssetResult result = service.processAsset("test");

		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(true, result.isSucess());
		assertEquals(12, result.getValue());
		assertEquals(true, result.isChange());
	}

	@Test
	public void testSimpleSingleCircularFormulaProcessAsset() {

		AssetResult result = service.CreateNewAssetWithFormula("test", 0, "if (test == 0) return 11; return test + 1;");

		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(true, result.isSucess());
		assertEquals(11, result.getValue());
		assertEquals(true, result.isChange());

		result = service.processAsset("test");

		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(true, result.isSucess());
		assertEquals(12, result.getValue());
		assertEquals(true, result.isChange());
	}

	@Test
	public void testCreateNewAssetSingleCircularFormulaWithAttrib() {

		AssetResult result = service.CreateNewAssetWithFormula("test", 0, "if(test == 0) test = 11; return test + 1;");

		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(true, result.isSucess());
		assertEquals(12, result.getValue());
		assertEquals(true, result.isChange());
	}

	@Test
	public void testCreateNewAssetSingleCircularFormulaTenario() {

		AssetResult result = service.CreateNewAssetWithFormula("test", 0, "test == 0 ? test = 11 : test++");

		System.out.println(result.getException());
		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(true, result.isSucess());
		assertEquals(11, result.getValue());
		assertEquals(true, result.isChange());
	}

	@Test
	public void testComplexProcessAsset() {

		AssetResult result = service.processAsset("a");

		System.out.println(result.getErrors());
		System.out.println(result.getAsset());
		assertEquals(true, result.isSucess());
		assertEquals(13, result.getValue());
		assertEquals(false, result.isChange());
	}

	@Test
	public void testUpdateAsset() {

		service.CreateNewAsset("A", 5.5);
		service.CreateNewAsset("B", 10);

		AssetResult resultDivision = service.CreateNewAssetWithFormula("division", 0.0, "(A + B)/2");

		System.out.println(resultDivision.getErrors());
		System.out.println(resultDivision.getAsset());
		assertEquals(true, resultDivision.isSucess());
		assertEquals(7.75, resultDivision.getValue());
		assertEquals(true, resultDivision.isChange());
		
		service.updateValueIn("A",(Double)14.3);
		service.updateValueIn("B",(Integer)17);
		resultDivision = service.processAsset("division");
		assertEquals(true, resultDivision.isSucess());
		assertEquals(15.65, resultDivision.getValue());
		assertEquals(true, resultDivision.isChange());

		Asset division = resultDivision.getAsset();
		for (HistoryValue<?> value : division.getValue().getHistory()) {
			System.out.println(value);
		}
	}

}
