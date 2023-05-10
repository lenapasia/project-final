package com.javarush.jira.profile.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.MatcherFactory;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.Profile;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.USER_ID;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static com.javarush.jira.profile.web.ProfileRestController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {

    private static final MatcherFactory.Matcher<ProfileTo> PROFILE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
            ProfileTo.class, "lastLogin", "contacts.id");

    @Autowired
    ProfileRepository repository;

    @Autowired
    ProfileMapper mapper;

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void get() throws Exception {
        final Profile dbProfile = repository.getExisted(USER_ID);
        final ProfileTo expectedResult = mapper.toTo(dbProfile);

        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_MATCHER.contentJson(expectedResult));
    }

    @Test
    void updateUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void update() throws Exception {
        final Set<String> mailNotifications = Stream.of("assigned", "deadline", "one_day_before_deadline", "two_days_before_deadline")
                .collect(Collectors.toCollection(HashSet::new));

        final Set<ContactTo> contacts = Stream.of(
                        new ContactTo("skype", "userSkypeUpdated")
                )
                .collect(Collectors.toCollection(HashSet::new));

        final ProfileTo updatedTo = new ProfileTo(USER_ID, mailNotifications, contacts);

        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        // profile should be changed to new values
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_MATCHER.contentJson(updatedTo));
    }

    @Test
    @WithUserDetails(value = USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateInvalidMailNotifications() throws Exception {
        final Set<String> mailNotifications = Stream.of("assigned1")
                .collect(Collectors.toCollection(HashSet::new));

        final ProfileTo updatedTo = new ProfileTo(USER_ID, mailNotifications, null);

        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateInvalidContact() throws Exception {
        final Set<ContactTo> contacts = Stream.of(
                        new ContactTo("skype1", "userSkypeUpdated")
                )
                .collect(Collectors.toCollection(HashSet::new));

        final ProfileTo updatedTo = new ProfileTo(USER_ID, null, contacts);

        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}