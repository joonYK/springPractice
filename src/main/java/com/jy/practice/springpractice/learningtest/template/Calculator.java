package com.jy.practice.springpractice.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(String filepath) throws IOException {
        BufferedReaderCallback sumCallback = new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                br = new BufferedReader(new FileReader(filepath));
                Integer sum = 0;
                String line = null;
                while((line = br.readLine()) !=  null) {
                    sum += Integer.parseInt(line);
                }

                return sum;
            }
        };

        return fileReadTmeplate(filepath, sumCallback);

    }

    public Integer calcMultiply(String filepath) throws IOException {
        BufferedReaderCallback multiplyCallback = new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                br = new BufferedReader(new FileReader(filepath));
                Integer multiply = 1;
                String line = null;
                while((line = br.readLine()) !=  null) {
                    multiply *= Integer.parseInt(line);
                }

                return multiply;
            }
        };

        return fileReadTmeplate(filepath, multiplyCallback);

    }

    public Integer fileReadTmeplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            return callback.doSomethingWithReader(br);

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
