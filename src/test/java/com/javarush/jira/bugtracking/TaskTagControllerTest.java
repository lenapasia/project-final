package com.javarush.jira.bugtracking;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.javarush.jira.bugtracking.TaskTagController.REST_URL;
import static com.javarush.jira.bugtracking.TaskTestData.TASK_ID;
import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskTagControllerTest extends AbstractControllerTest {

    private static final long TASK_ID = 2;

    @Autowired
    TaskRepository repository;

    @Test
    void update_shouldThrowUnauthorizedException_whenUserIsNotAuthorized() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/{id}/tags", TASK_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void update_shouldStoreTags() throws Exception {
        final Set<String> newTags = Stream.of("tag1", "tag2").collect(Collectors.toCollection(HashSet::new));

        perform(MockMvcRequestBuilders.put(REST_URL + "/{id}/tags", TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTags)))
                .andDo(print())
                .andExpect(status().isNoContent());

        // check that tags were saved into database
        final Task task = repository.getExisted(TASK_ID);
        assertThat(task.getTags()).usingRecursiveComparison().isEqualTo(newTags);
    }

    @Test
    @WithUserDetails(value = USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void update_shouldClearTags_whenGivenEmpty() throws Exception {
        // set new tags
        final Set<String> newTags = Stream.of("tag1", "tag2").collect(Collectors.toCollection(HashSet::new));
        perform(MockMvcRequestBuilders.put(REST_URL + "/{id}/tags", TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTags)))
                .andDo(print())
                .andExpect(status().isNoContent());

        // check that tags were saved into database
        final Task task = repository.getExisted(TASK_ID);
        assertThat(task.getTags()).usingRecursiveComparison().isEqualTo(newTags);

        // clear tags
        final Set<String> emptyTags = new HashSet<>();
        perform(MockMvcRequestBuilders.put(REST_URL + "/{id}/tags", TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(emptyTags)))
                .andDo(print())
                .andExpect(status().isNoContent());

        final Task updatedTask = repository.getExisted(TASK_ID);
        assertThat(updatedTask.getTags()).usingRecursiveComparison().isEqualTo(emptyTags);
    }

    @Test
    @WithUserDetails(value = USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void update_shouldStoreOnlyUniqueTags_whenGivenDuplicates() throws Exception {
        final List<String> newTags = Arrays.asList("tag1", "tag2", "tag1");

        perform(MockMvcRequestBuilders.put(REST_URL + "/{id}/tags", TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTags)))
                .andDo(print())
                .andExpect(status().isNoContent());

        final Set<String> expectedTags = new HashSet<>(Arrays.asList("tag1", "tag2"));
        final Task task = repository.getExisted(TASK_ID);
        assertThat(task.getTags()).usingRecursiveComparison().isEqualTo(expectedTags);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update_shouldThrowException_whenRequestBodyIsMissing() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/{id}/tags", -1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(null)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update_shouldThrowNotFoundException_whenTaskDoesNotExist() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/{id}/tags", -1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(new HashSet<>())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update_shouldThrowException_whenGivenTagIsInvalid() throws Exception {
        final Set<String> invalidTags = Set.of("t", "tag2");
        perform(MockMvcRequestBuilders.put(REST_URL + "/{id}/tags", TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(invalidTags)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}