package com.jy.practice.springpractice.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(String filepath) throws IOException {
        LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) throws IOException {
                value += Integer.parseInt(line);
                return value;
            }
        };

        return lineReadTmeplate(filepath, sumCallback, 0);

    }

    public Integer calcMultiply(String filepath) throws IOException {
        LineCallback<Integer> multiplyCallback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) throws IOException {
                value *= Integer.parseInt(line);
                return value;
            }
        };

        return lineReadTmeplate(filepath, multiplyCallback, 1);

    }

    public String concatenate(String filepath) throws IOException {
        LineCallback<String> concatenateCallback = new LineCallback<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) throws IOException {
                return value + line;
            }
        };
        return lineReadTmeplate(filepath, concatenateCallback, "");
    }

    public <T> T lineReadTmeplate(String filepath, LineCallback<T> callback, T initVal) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            T res = initVal;
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
