package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.common.error.IllegalRequestDataException;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskSubscriptionService {

    private final UserBelongRepository userBelongRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addSubscription(long taskId, long userId) {
        final Task task = taskRepository.getExisted(taskId);
        final User user = userRepository.getExisted(userId);

        checkAssignment(taskId, userId);
        checkSubscription(taskId, userId);

        task.addSubscribedUser(user);
        taskRepository.save(task);
    }

    private void checkAssignment(long taskId, long userId) {
        final boolean assignmentExists =
                userBelongRepository.existsByObjectIdAndObjectTypeAndUserId(taskId, ObjectType.TASK, userId);

        if (assignmentExists) {
            throw new IllegalRequestDataException(String.format(
                    "Unable to subscribe user with id '%d' to task with id '%d': this task already assigned on this user.",
                    taskId, userId));
        }
    }

    private void checkSubscription(long taskId, long userId) {
        final boolean subscriptionExists = taskRepository.existsByIdAndSubscribedUsersId(taskId, userId);

        if (subscriptionExists) {
            throw new IllegalRequestDataException(String.format(
                    "Unable to subscribe user with id '%d' to task with id '%d': this user is already subscribed to this task.",
                    taskId, userId));
        }
    }
}
