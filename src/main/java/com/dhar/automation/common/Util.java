package com.dhar.automation.common;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dharmendra.Singh
 */
public class Util {

    /**
     * This method formatted time duration between startTime and endTime.
     * @param startTime starttime
     * @param endTime endtime
     * @return difference between startTime and endTime followed by unit.
     */
    public static String formatTime(long startTime, long endTime){
        return ((endTime - startTime)/1000000) + " ms";
    }

    public static Map<String, String> getObjectFromJsonArray(String json, String jsonKey, String jsonKeyValue){
        Map<String, String> result = new HashMap<>();
        try{
            JsonArray jsonArray = new GsonBuilder().create().fromJson(json, JsonArray.class);
            for(int i = 0; i < jsonArray.size(); i++){
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                if(jsonObject.has(jsonKey) && jsonObject.get(jsonKey).getAsString().equals(jsonKeyValue)){
                    for(Map.Entry<String, JsonElement> jsonElement : jsonObject.entrySet()){
                        result.put(jsonElement.getKey(), jsonElement.getValue().getAsString());
                    }
                }
            }
        }catch(Exception e){}

        return result;
    }

    public static String stackTraceToString(Throwable throwable){
        return ExceptionUtils.getStackTrace(throwable);
    }

    public static String getOptionalString(String value, String defaultValue){
        if(StringUtils.isNotEmpty(value)){
            return value;
        } else {
            return defaultValue;
        }
    }

}
