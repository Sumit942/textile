package com.example.textile.utility.factory;

import com.example.textile.executors.ActionExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ActionExecutorFactory {

    private static ActionExecutorFactory factory = new ActionExecutorFactory();

    private ActionExecutorFactory() {
        log.debug("ActionExecutorFactory initialized!!");
        log.info("{} initialzzzzzzz",this.getClass().getName());
        actionExecutorsMap = new HashMap<>();
    }

    public static ActionExecutorFactory getFactory() {
        return factory;
    }

    private static Map<Class<?>, Map<String, ActionExecutor>> actionExecutorsMap;

    public static Map<String, ActionExecutor> getActionExecutors(Class<?> cls) {

        Map<String, ActionExecutor> actExecutorMap = actionExecutorsMap.get(cls);
        if (actExecutorMap == null) {
            actExecutorMap = new HashMap<>();
            actionExecutorsMap.put(cls, actExecutorMap);
        }

        return actExecutorMap;
    }
}
