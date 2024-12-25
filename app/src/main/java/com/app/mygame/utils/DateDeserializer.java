package com.app.mygame.utils;

import android.annotation.SuppressLint;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return DATE_FORMAT.parse(json.getAsString());
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }
}
