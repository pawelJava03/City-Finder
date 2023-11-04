package com.example.thymeleafproject.readAPI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;

public class KeyReader {
    public static String getKey(){
        String apiKey = null;
        try{
            BufferedReader reader = new BufferedReader(new FileReader("apikey.txt"));
            apiKey = reader.readLine();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return apiKey;
    }
}
