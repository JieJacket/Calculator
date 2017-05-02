package com.example.jiewu.calculator;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.jiewu.calculator.util.Calculator;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.jiewu.calculator", appContext.getPackageName());
    }

    @Test
    public void testCalculator() {
        Calculator calculator = new Calculator();
        calculator.setInput("134+412x31+94÷213");
        List<Object> list = calculator.formatInput();
        Object[] result = list.toArray();
        Object[] o = {134d, "+", 412d, "x", 31d, "+", 94d, "÷", 213d};
        assertArrayEquals(result, o);

    }
}
