package com.example.textile.utility.factory;

import com.example.textile.executors.ActionExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ActionExecutorFactory {

    private final static ActionExecutorFactory factory = new ActionExecutorFactory();

    private ActionExecutorFactory() {
        log.debug("ActionExecutorFactory initialized!!");
        actionExecutorsMap = new HashMap<>();
    }

    public static ActionExecutorFactory getFactory() {
        return factory;
    }

    private static Map<Class<?>, Map<String, ActionExecutor>> actionExecutorsMap;

    public Map<String, ActionExecutor> getActionExecutors(Class<?> cls) {

        Map<String, ActionExecutor> actExecutorMap = actionExecutorsMap
                .computeIfAbsent(cls, k -> new HashMap<>());

        return actExecutorMap;
    }
}
