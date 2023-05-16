package com.javarush.jira.bugtracking.internal.model;

import java.util.HashMap;
import java.util.Map;

public enum TaskStatus {
    READY("ready"),
    IN_PROGRESS("in progress"),
    DONE("done");



    private static final Map<String, TaskStatus> CODE_TO_STATUS = new HashMap<>();

    static {
        for (TaskStatus e : values()) {
            CODE_TO_STATUS.put(e.code, e);
        }
    }

    private final String code;

    TaskStatus(String code) {
        this.code = code;
    }

    public static TaskStatus valueOfCode(String code) {
        return CODE_TO_STATUS.get(code);
    }

    public String getCode() {
        return code;
    }
}
