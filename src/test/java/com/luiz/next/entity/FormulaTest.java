package com.luiz.next.entity;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class FormulaTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetAplusBDependencies() {

		Formula ab = new Formula("a+b");
		Formula a_b = new Formula("a + b");
		Formula _ab = new Formula("(a + b)");
		
		Formula complex = new Formula("((a + b)/2 + 40) = !not - not = * &# == complex comp: ortho? <asde.!<");
		
		assertEquals("[a, b]", ab.loadDependencies().toString());
		assertEquals("[a, b]", a_b.loadDependencies().toString());
		assertEquals("[a, b]", _ab.loadDependencies().toString());
		assertEquals("[a, comp, b, not, complex, ortho, asde]", complex.loadDependencies().toString());
	}
	
	@Test
	public void testReservedWords() {

		Formula complex = new Formula("if(test == 0) return 11; return test + 1;");
		Set<String> loadAsset = complex.loadDependencies();
		System.out.println(loadAsset);
		assertEquals("[test]", loadAsset.toString());
	}

}
