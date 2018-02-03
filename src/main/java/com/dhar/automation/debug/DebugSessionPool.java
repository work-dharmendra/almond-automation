package com.dhar.automation.debug;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dharmendra Chouhan
 */
@Component
public class DebugSessionPool {

    private Map<String, DebugSession> debugSessionMap = new HashMap<>();

    public String addDebugSessionNew(DebugSession debugSession){
        String uuid = UUID.randomUUID().toString();
        debugSession.setUuid(uuid);
        debugSessionMap.put(uuid, debugSession);

        return uuid;
    }

    public DebugSession getDebugSession(String uuid){
        return debugSessionMap.get(uuid);
    }

    public void remove(String key) {
        debugSessionMap.remove(key);
    }
}
