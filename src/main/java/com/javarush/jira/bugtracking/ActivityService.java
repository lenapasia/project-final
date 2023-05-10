package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.model.Activity;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.model.TaskStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ActivityService {

    public Duration calculateWorkingTime(Task task) {
        return calculateTime(task, TaskStatus.IN_PROGRESS, TaskStatus.READY);
    }

    public Duration calculateTestingTime(Task task) {
        return calculateTime(task, TaskStatus.READY, TaskStatus.DONE);
    }

    private Duration calculateTime(Task task, TaskStatus startStatus, TaskStatus endStatus) {
        final List<Activity> activities = task.getActivities();

        final Map<TaskStatus, Activity> statusToActivity = new HashMap<>();
        for (Activity eachActivity : activities) {
            final TaskStatus status = TaskStatus.valueOfCode(eachActivity.getStatusCode());
            if (status != null) {
                statusToActivity.put(status, eachActivity);
            }
        }

        final Optional<Activity> startActivity = Optional.ofNullable(statusToActivity.get(startStatus));
        final Optional<Activity> endActivity = Optional.ofNullable(statusToActivity.get(endStatus));

        if (startActivity.isPresent() && endActivity.isPresent()) {
            final LocalDateTime start = startActivity.get().getUpdated();
            final LocalDateTime end = endActivity.get().getUpdated();

            return Duration.between(start, end);
        }
        return null;
    }
}
