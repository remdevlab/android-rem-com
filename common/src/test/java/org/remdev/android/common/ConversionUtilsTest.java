package org.remdev.android.common;

import com.google.gson.annotations.SerializedName;

import org.junit.Assert;
import org.junit.Test;
import org.remdev.android.common.utils.ConversionUtils;
import org.remdev.android.common.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

public class ConversionUtilsTest {

    @Test
    public void testToJson() {
        String json = ConversionUtils.toJson(new TestPojo());
        Assert.assertEquals(TestPojo.TEST_JSON, json);
    }

    @Test
    public void testFromJson() {
        TestPojo result = ConversionUtils.fromJson(TestPojo.TEST_JSON, TestPojo.class);
        Assert.assertEquals(new TestPojo(), result);
    }

    @Test
    public void testFromJsonToList() {
        List<String> testPojos = Arrays.asList(TestPojo.TEST_JSON, TestPojo.TEST_JSON);
        String joined = StringUtils.join(",", testPojos);
        joined = String.format("[%s]", joined);
        List<TestPojo> result = ConversionUtils.fromJsonToList(joined, TestPojo.class);
        Assert.assertTrue(result.size() == testPojos.size());
        for (TestPojo testPojo : result) {
            Assert.assertEquals(new TestPojo(), testPojo);
        }
    }

    @Test
    public void testParseInt() {
        int testVal = 123;
        int altVal = -5;
        Assert.assertEquals(ConversionUtils.parseInt(String.valueOf(testVal), altVal), testVal);
        Assert.assertNotEquals(ConversionUtils.parseInt(String.valueOf(testVal), altVal), altVal);
        Assert.assertEquals(ConversionUtils.parseInt(String.valueOf(testVal) + "asd", altVal), altVal);
    }

    @Test
    public void testParseDouble() {
        double testVal = 123.45;
        double altVal = -15d;
        Assert.assertEquals(ConversionUtils.parseDouble(String.valueOf(testVal), altVal), testVal, 0);
        Assert.assertNotEquals(ConversionUtils.parseDouble(String.valueOf(testVal), altVal), altVal, 0);
        Assert.assertEquals(ConversionUtils.parseDouble(String.valueOf(testVal) + "asd", altVal), altVal, 0);
    }

    private static class TestPojo {

        private static final String STRING_PROPERTY_VAL = "stringProperty";
        private static final String RENAMED_PROPERTY_VAL = "renamedProperty";
        private static final int INT_PROPERTY_VAL = 1;

        private static final String TEST_JSON = new StringBuilder()
                .append('{')
                    .append('"').append("property1").append('"')
                    .append(':')
                    .append('"').append(STRING_PROPERTY_VAL).append('"')
                .append(',')
                    .append('"').append("property2").append('"')
                    .append(':')
                    .append(INT_PROPERTY_VAL)
                .append(',')
                    .append('"').append("prop3").append('"')
                    .append(':')
                    .append('"').append(RENAMED_PROPERTY_VAL).append('"')
                .append("}").toString();


        private String property1 = STRING_PROPERTY_VAL;
        private int property2 = INT_PROPERTY_VAL;
        @SerializedName("prop3")
        private String property3 = RENAMED_PROPERTY_VAL;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestPojo testPojo = (TestPojo) o;

            if (property2 != testPojo.property2) return false;
            if (property1 != null ? !property1.equals(testPojo.property1) : testPojo.property1 != null)
                return false;
            return property3 != null ? property3.equals(testPojo.property3) : testPojo.property3 == null;

        }

        @Override
        public int hashCode() {
            int result = property1 != null ? property1.hashCode() : 0;
            result = 31 * result + property2;
            result = 31 * result + (property3 != null ? property3.hashCode() : 0);
            return result;
        }
    }
}
