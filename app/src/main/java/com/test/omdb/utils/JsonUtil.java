package com.test.omdb.utils;

import com.google.gson.Gson;

import java.io.IOException;

public class JsonUtil {
    public static <T> T fromJson(String searlized, Class<T> clazz) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(searlized, clazz);
    }
}
