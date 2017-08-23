package org.remdev.android.common;

import org.junit.Assert;
import org.junit.Test;
import org.remdev.android.common.utils.StringUtils;

import java.util.Arrays;

public class StringUtilsTest {

    @Test
    public void testBlankCheck() {
        Assert.assertTrue(StringUtils.isBlank(null));
        Assert.assertTrue(StringUtils.isBlank(""));
        Assert.assertFalse(StringUtils.isBlank(" "));
    }

    @Test
    public void testNotBlankCheck() {
        Assert.assertFalse(StringUtils.isNotBlank(null));
        Assert.assertFalse(StringUtils.isNotBlank(""));
        Assert.assertTrue(StringUtils.isNotBlank(" "));
    }

    @Test
    public void testEqualsOrBlankCheck() {
        Assert.assertTrue(StringUtils.equalsOrBlank(null, null));
        Assert.assertTrue(StringUtils.equalsOrBlank(null, ""));
        Assert.assertTrue(StringUtils.equalsOrBlank("", ""));
        Assert.assertTrue(StringUtils.equalsOrBlank("", null));
        Assert.assertTrue(StringUtils.equalsOrBlank("asd", "asd"));
        Assert.assertFalse(StringUtils.equalsOrBlank("asd", ""));
        Assert.assertFalse(StringUtils.equalsOrBlank("asd", null));
        Assert.assertFalse(StringUtils.equalsOrBlank("AsD", "asd"));
        Assert.assertFalse(StringUtils.equalsOrBlank(null, "asd"));
        Assert.assertFalse(StringUtils.equalsOrBlank("", "asd"));
        Assert.assertFalse(StringUtils.equalsOrBlank("asd", "AsD"));
    }

    @Test
    public void testEqualsOrBlankIgnoringCaseCheck() {
        Assert.assertTrue(StringUtils.equalsOrBlankIgnoreCase(null, null));
        Assert.assertTrue(StringUtils.equalsOrBlankIgnoreCase(null, ""));
        Assert.assertTrue(StringUtils.equalsOrBlankIgnoreCase("", ""));
        Assert.assertTrue(StringUtils.equalsOrBlankIgnoreCase("", null));
        Assert.assertTrue(StringUtils.equalsOrBlankIgnoreCase("asd", "asd"));
        Assert.assertFalse(StringUtils.equalsOrBlankIgnoreCase("asd", ""));
        Assert.assertFalse(StringUtils.equalsOrBlankIgnoreCase("asd", null));
        Assert.assertTrue(StringUtils.equalsOrBlankIgnoreCase("AsD", "asd"));
        Assert.assertFalse(StringUtils.equalsOrBlankIgnoreCase(null, "asd"));
        Assert.assertFalse(StringUtils.equalsOrBlankIgnoreCase("", "asd"));
        Assert.assertTrue(StringUtils.equalsOrBlankIgnoreCase("asd", "AsD"));
    }

    @Test
    public void testLeftPad() {
        String source = "1234";
        Assert.assertEquals(source, StringUtils.leftPad(source, 4, ' '));
        Assert.assertEquals(source, StringUtils.leftPad(source, 1, ' '));
        String expected = "000" + source;
        Assert.assertEquals(expected, StringUtils.leftPad(source, expected.length(), '0'));
        Assert.assertNotEquals(expected, StringUtils.leftPad(source, expected.length(), '1'));
    }

    @Test
    public void testRightPad() {
        String source = "1234";
        Assert.assertEquals(source, StringUtils.rightPad(source, 4, ' '));
        Assert.assertEquals(source, StringUtils.rightPad(source, 1, ' '));
        String expected = source + "0000";
        Assert.assertEquals(expected, StringUtils.rightPad(source, expected.length(), '0'));
        Assert.assertNotEquals(expected, StringUtils.rightPad(source, expected.length(), '1'));
    }

    @Test
    public void testJoinArray() {
        String expected = "One,Two,Three,Four";
        String[] words = expected.split(",");
        Assert.assertEquals(expected, StringUtils.join(",", words));
        Assert.assertNotEquals(expected, StringUtils.join(" ", words));
    }

    @Test
    public void testJoinIterable() {
        String expected = "One,Two,Three,Four";
        String[] words = expected.split(",");
        Assert.assertEquals(expected, StringUtils.join(",", Arrays.asList(words)));
        Assert.assertNotEquals(expected, StringUtils.join(" ", Arrays.asList(words)));
    }
}
