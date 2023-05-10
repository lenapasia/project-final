package com.javarush.jira.bugtracking;


import com.javarush.jira.login.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = TaskTagController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class TaskTagController {

    public static final String REST_URL = "/api/bugtracking/tasks";

    private final TaskService taskService;

    @PutMapping(value = "/{id}/tags", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long id, @RequestBody Set<String> tags, @AuthenticationPrincipal AuthUser authUser) {
        log.debug("user[id={}] update tags for task[id = {}]: {}", authUser.id(), id, tags);

        taskService.updateTags(id, tags);
    }
}
