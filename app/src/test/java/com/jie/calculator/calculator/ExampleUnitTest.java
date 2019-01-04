package com.jie.calculator.calculator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private List<String> list;
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test(){
        list = new ArrayList<>();
        list.add("1");
        list.add("2");
        update(list);
    }

    public void update(List<String> data){
        list.clear();
        list.addAll(data);
        System.out.println(list.size());
    }
}