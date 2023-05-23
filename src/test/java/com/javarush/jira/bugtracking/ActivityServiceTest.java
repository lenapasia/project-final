package com.javarush.jira.bugtracking;

import com.javarush.jira.AbstractTest;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityServiceTest extends AbstractTest {

    public static final long TASK_ID = 4;

    @Autowired
    ActivityService service;

    @Autowired
    TaskRepository taskRepository;

    @Test
    @Transactional
    void calculateWorkingTime() {
        final Duration actualWorkingTime = service.calculateWorkingTime(TASK_ID);
        final Duration expected = Duration.ofMinutes(2*60 + 25);
        assertThat(actualWorkingTime).isEqualTo(expected);
    }

    @Test
    @Transactional
    void calculateTestingTime() {
        final Duration actualTestingTime = service.calculateTestingTime(TASK_ID);
        final Duration expected = Duration.parse("PT25M5S");
        assertThat(actualTestingTime).isEqualTo(expected);
    }

    @Test
    @Transactional
    void calculateWhenTaskHasNoActivities() {
        final long taskIdWithoutActivities = TaskTestData.TASK_ID;
        final Duration actualWorkingTime = service.calculateWorkingTime(taskIdWithoutActivities);
        assertThat(actualWorkingTime).isNull();
        final Duration actualTestingTime = service.calculateTestingTime(taskIdWithoutActivities);
        assertThat(actualTestingTime).isNull();
    }

    @Test
    @Transactional
    void calculateWhenTaskHasOnlyStartingActivity() {
        final long taskIdWithOnlyStartingActivity = 3;
        final Task task = taskRepository.getExisted(taskIdWithOnlyStartingActivity);
        assertThat(task.getActivities()).isNotNull().isNotEmpty();
        final Duration actualWorkingTime = service.calculateWorkingTime(task.id());
        assertThat(actualWorkingTime).isNull();
    }

}