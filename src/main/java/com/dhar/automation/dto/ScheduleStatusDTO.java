package com.dhar.automation.dto;

import com.dhar.automation.RunType;
import com.dhar.automation.domain.Schedule;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dharmendra.singh
 */
public class ScheduleStatusDTO {

    public Schedule schedule;
    public Long scheduleId;
    public Long testCaseSuiteId;
    public String testCasesIds;
    public RunType runType;

    public List<TestCaseExecutionStatusDTO> executionStatus = new ArrayList<>();

    @Override
    public String toString() {
        final Gson gson1 = new GsonBuilder().create();
        final Gson gson = new GsonBuilder().registerTypeAdapter(TestCaseExecutionStatusDTO.class, new JsonSerializer<TestCaseExecutionStatusDTO>() {
            @Override
            public JsonElement serialize(TestCaseExecutionStatusDTO testCaseExecutionStatusDTO, Type type, JsonSerializationContext jsonSerializationContext) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("scheduledId", testCaseExecutionStatusDTO.scheduleId);
                jsonObject.addProperty("testCaseId", testCaseExecutionStatusDTO.testCaseId);
                JsonArray jsonArray = new JsonArray();

                for(String s : testCaseExecutionStatusDTO.status){
                    jsonArray.add(gson1.fromJson(s, JsonObject.class));
                }

                jsonObject.add("steps", jsonArray);

                return jsonObject;
            }
        }).create();
        return gson.toJson(this);
    }
}
