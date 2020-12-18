package me.gowoo.studyrestapi.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("Test")
public class AccountServiceTest {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Test
    public void findByEmail(){
        //given
        String password = "1234";
        String email = "12@naver.com";
        Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN,AccountRole.USER))
                .build();
        this.accountRepository.save(account);
        //When
        UserDetailsService userDetailsService = (UserDetailsService)accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        //Then
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }
}