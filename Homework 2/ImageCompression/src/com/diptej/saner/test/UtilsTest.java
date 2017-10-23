package com.diptej.saner.test;

import org.junit.Assert;
import org.junit.Test;

import com.diptej.saner.Utils;

public class UtilsTest {

	@Test
	public void testGetPixelFromRGB() {
		// int r = -30, g = -119, b = 125;
		int r = 226, g = 137, b = 125;
		int expected = -1930883;

		int actual = Utils.getPixelFromRGB(r, g, b);

		Assert.assertEquals(expected, actual);
	}
}
