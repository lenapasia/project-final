package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.model.Activity;
import com.javarush.jira.bugtracking.internal.model.TaskStatus;
import com.javarush.jira.bugtracking.internal.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository repository;

    public Duration calculateWorkingTime(long taskId) {
        return calculateTime(taskId, TaskStatus.IN_PROGRESS, TaskStatus.READY);
    }

    public Duration calculateTestingTime(long taskId) {
        return calculateTime(taskId, TaskStatus.READY, TaskStatus.DONE);
    }

    private Duration calculateTime(long taskId, TaskStatus startStatus, TaskStatus endStatus) {
        final List<Activity> activities = repository.getByStatusCodeInAndTaskId(
                Set.of(startStatus.getCode(), endStatus.getCode()), taskId);

        final Optional<Activity> startActivity = findByStatus(activities, startStatus);
        final Optional<Activity> endActivity = findByStatus(activities, endStatus);

        if (startActivity.isPresent() && endActivity.isPresent()) {
            final LocalDateTime start = startActivity.get().getUpdated();
            final LocalDateTime end = endActivity.get().getUpdated();

            return Duration.between(start, end);
        }
        return null;
    }

    private Optional<Activity> findByStatus(List<Activity> activities, TaskStatus status) {
        return activities.stream().filter(activity -> status.getCode().equalsIgnoreCase(activity.getStatusCode()))
                .findAny();
    }
}
