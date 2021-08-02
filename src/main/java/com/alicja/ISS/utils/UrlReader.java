package com.alicja.ISS.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exception.SearchException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;


public class UrlReader {

    public  JsonObject convertURLStringIntoJsonObject(String URL) throws IOException {
        URL url = new URL(URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        if(conn.getResponseCode()>400){
            throw  new SearchException("Connection could not be established");
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line;
        StringBuilder sb = new StringBuilder();
        if((line = bufferedReader.readLine())!=null){
            sb.append(line);
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(line).getAsJsonObject();
        return jsonObject;

    }
}
