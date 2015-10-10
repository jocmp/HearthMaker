package edu.gvsu.cis.campbjos.hearthstonebuilder;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
    import org.junit.Test;

    import com.vogella.android.temperature.ConverterUtil;

public class JsonUtilTest {

    @Test
    public void testConvertFahrenheitToCelsius() {
        float actual = ConverterUtil.convertCelsiusToFahrenheit(100);
        // expected value is 212
        float expected = 212;
        // use this method because float is not precise
        assertEquals("Conversion from celsius to fahrenheit failed", expected,
            actual, 0.001);
    }

    @Test
    public void testConvertCelsiusToFahrenheit() {
        float actual = ConverterUtil.convertFahrenheitToCelsius(212);
        // expected value is 100
        float expected = 100;
        // use this method because float is not precise
        assertEquals("Conversion from celsius to fahrenheit failed", expected,
            actual, 0.001);
    }

}
