package com.dhar.automation.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author Dharmendra.Singh
 */
public class UtilTest {

    @Test
    public void getObjectFromJsonArray_JsonArrayStringAndJsonKeyAndJsonKeyValue_KeyPresentInArray_ReturnCorrectMap(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "valueOfKey");
        jsonObject.addProperty("value", "value1");
        jsonObject.addProperty("type", "password");
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject);

        Map<String, String> actual = Util.getObjectFromJsonArray(jsonArray.toString(), "key", "valueOfKey");
        Assert.assertEquals("valueOfKey", actual.get("key"));
        Assert.assertEquals("value1", actual.get("value"));

    }
}
