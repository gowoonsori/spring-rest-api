package me.gowoo.studyrestapi.configs;

import me.gowoo.studyrestapi.accounts.AccountRepository;
import me.gowoo.studyrestapi.accounts.AccountService;
import me.gowoo.studyrestapi.common.AppProperties;
import me.gowoo.studyrestapi.common.BaseTest;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class AuthServerConfigTest extends BaseTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;

    @Test
    @DisplayName("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception{
        //Given
        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(appProperties.getClientId(),appProperties.getClientSecret()))
                    .param("username",appProperties.getUserUsername())
                    .param("password",appProperties.getUserPassword())
                    .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }

}