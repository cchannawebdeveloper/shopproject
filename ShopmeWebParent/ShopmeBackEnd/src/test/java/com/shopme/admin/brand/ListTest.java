package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ListTest {
	
	@Test
	public void testAssertList() {
		List<String> actual = Arrays.asList("a", "b", "c");
		List<String> expected = Arrays.asList("a", "b", "c", "d");
		
		List<String> words = Arrays.asList("available", "avenge", "avenue", "average");
		
		for( String list : words ) {
			assertTrue(list.startsWith("av"));
		}
		
		//assertEquals(expected, actual);
		
		//assertEquals(123, 123);
		//assertEquals("abc", "abc");
		//assertThat("test", equals("test2")); 
		
		//assertThat(actual, is(equalTo(expected)));
		
		//1. Test equal.
		
		//(actual, is(expected));
		
		//2. If List has this value?
       // assertThat(actual, hasItems(String strings = "B"));
	}

}
