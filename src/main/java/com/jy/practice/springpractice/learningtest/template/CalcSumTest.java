package com.jy.practice.springpractice.learningtest.template;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class CalcSumTest {
    private Calculator calculator;
    private String numFilepath;

    @Before
    public void setUp() {
        calculator = new Calculator();
        numFilepath = getClass().getResource("numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException {
        assertThat(calculator.calcSum(numFilepath), is(10));
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        assertThat(calculator.calcMultiply(numFilepath), is(24));
    }
}
