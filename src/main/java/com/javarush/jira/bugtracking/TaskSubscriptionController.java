package com.javarush.jira.bugtracking;

import com.javarush.jira.login.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = TaskSubscriptionController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskSubscriptionController {

    public static final String REST_URL = "/api/bugtracking/tasks";

    private final TaskSubscriptionService subscriptionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{id}/subscription")
    public void addSubscription(@PathVariable long id, @AuthenticationPrincipal AuthUser authUser) {
        log.debug("user[id={}] add subscription for task[id = {}]", authUser.id(), id);

        subscriptionService.addSubscription(id, authUser.id());
    }
}
