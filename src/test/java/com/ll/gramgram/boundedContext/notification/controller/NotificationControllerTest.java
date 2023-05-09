package com.ll.gramgram.boundedContext.notification.controller;

import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("알림 목록에 접속시 'readDate : null -> now()'로 읽음처리 한다")
    @WithUserDetails("user4")
    void test001() throws Exception {

        List<Notification> notifications = notificationService.findByToInstaMember_username("insta_user4");

        long readCount = notifications
                .stream()
                .filter(notification -> !notification.isRead()) // 읽지 않은 것
                .count();

        assertThat(readCount).isEqualTo(3);

        ResultActions resultActions = mvc
                .perform(get("/usr/notification/list"))
                .andDo(print());

        readCount = notifications
                .stream()
                .filter(notification -> !notification.isRead())
                .count();

        assertThat(readCount).isEqualTo(0);
    }

    @Test
    @DisplayName("알림이 있을시 인디케이트 표시")
    @WithUserDetails("user4")
    void test002() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/home/about"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        data-test="hasUnreadNotifications"
                        """.stripIndent().trim())));
    }

    @Test
    @DisplayName("알림이 없을시 인디케이트 표시 X")
    @WithUserDetails("user4")
    void test003() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/notification/list"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(not(containsString("""
                        data-test="hasUnreadNotifications"
                        """.stripIndent().trim()))));
    }
}