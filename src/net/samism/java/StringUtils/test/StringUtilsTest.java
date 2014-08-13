package net.samism.java.StringUtils.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.assertEquals;
import static net.samism.java.StringUtils.StringUtils.getIndicesOf;
import static org.junit.Assert.assertArrayEquals;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/13/14
 * Time: 3:09 AM
 */
public class StringUtilsTest {
	private final Logger log = LoggerFactory.getLogger(getClass().getName());

	public static void main(String[] args) {
		StringUtilsTest test = new StringUtilsTest();
		test.testGetIndicesOf();
	}

	@Test (expected=IllegalArgumentException.class)
	public void testGetIndicesOf() {
//		try {
		assertArrayEquals(getIndicesOf("tomorrow", "o"), new int[]{1, 3, 6});
		assertArrayEquals(getIndicesOf("tomorrow", null), null);
		assertArrayEquals(getIndicesOf("tomorrow", ""), null);
		assertArrayEquals(getIndicesOf("tomorrow", "z"), null);
		assertArrayEquals(getIndicesOf("", "tomorrow"), null);
		assertArrayEquals(getIndicesOf(null, "tomorrow"), null);
		assertArrayEquals(getIndicesOf(null, null), null);
//		} catch (Exception e) {
//			String expectedExceptionText = "token's length must not exceed searched string's length";
//			assertEquals(expectedExceptionText, e.getMessage());
//		}
	}
}