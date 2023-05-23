package com.javarush.jira.bugtracking;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.bugtracking.TaskSubscriptionController.REST_URL;
import static com.javarush.jira.bugtracking.TaskTestData.NOT_EXISTING_TASK_ID;
import static com.javarush.jira.bugtracking.TaskTestData.TASK_ID;
import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskSubscriptionControllerTest extends AbstractControllerTest {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void addSubscription_shouldThrowUnauthorizedException_whenUserIsNotAuthorized() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "/{id}/subscription", TASK_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void addSubscription_shouldThrowNotFoundException_whenTaskDoesNotExist() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "/{id}/subscription", NOT_EXISTING_TASK_ID))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addSubscription_shouldThrowException_whenTaskIsAlreadyAssigned() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "/{id}/subscription", TASK_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void addSubscription_shouldThrowException_whenUserIsAlreadySubscribedToTask() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "/{id}/subscription", 3))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL/*, setupBefore = TestExecutionEvent.TEST_EXECUTION*/)
    void addSubscription_shouldStoreSubscription() throws Exception {
        final long taskId = TASK_ID;

        perform(MockMvcRequestBuilders.post(TaskTagController.REST_URL + "/{id}/subscription", taskId))
                .andDo(print())
                .andExpect(status().isCreated());

        // check that subscription has been saved into database
        final User user = userRepository.findByEmailIgnoreCase(USER_MAIL).get();

        assertThat(taskRepository.existsByIdAndSubscribedUsersId(taskId, user.id()))
                .withFailMessage(() -> "Expecting new subscription will be saved into database but was not.")
                .isTrue();
    }

}