package com.jy.practice.springpractice.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(String filepath) throws IOException {
        LineCallback sumCallback = new LineCallback() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) throws IOException {
                value += Integer.parseInt(line);
                return value;
            }
        };

        return lineReadTmeplate(filepath, sumCallback, 0);

    }

    public Integer calcMultiply(String filepath) throws IOException {
        LineCallback multiplyCallback = new LineCallback() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) throws IOException {
                value *= Integer.parseInt(line);
                return value;
            }
        };

        return lineReadTmeplate(filepath, multiplyCallback, 1);

    }

    public Integer lineReadTmeplate(String filepath, LineCallback callback, Integer initVal) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            Integer res = initVal;
            String line = null;
            while((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }

            return res;

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {}
            }
        }
    }
}
