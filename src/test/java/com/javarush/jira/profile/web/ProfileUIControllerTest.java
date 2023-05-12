package com.javarush.jira.profile.web;

import com.javarush.jira.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.login.internal.web.UserTestData.*;
import static com.javarush.jira.profile.web.ProfileUIController.PROFILE_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileUIControllerTest extends AbstractControllerTest {

    @Test
    void getUnauthorized() throws Exception {
        final ResultActions resultActions = perform(MockMvcRequestBuilders.get(PROFILE_URL))
                .andExpect(status().isFound())
                .andDo(print());

        resultActions.andExpect(redirectedUrlPattern("**/view/login**"));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(PROFILE_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}