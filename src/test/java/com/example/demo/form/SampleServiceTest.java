package com.example.demo.form;

import com.example.demo.account.Account;
import com.example.demo.account.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleServiceTest {

    @Autowired
    SampleService sampleService;
    @Autowired
    AccountService accountService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Test
    public void dashboard() {
        Account account = new Account();
        account.setRole("USER");
        account.setUsername("soohyun");
        account.setPassword("123");
        accountService.createNew(account);

        //사용자가 입력한 username으로 UserDatails를 가져옵니다.
        //userDetails가 return 하는 값이 Principal 입니다.
        UserDetails userDetails = accountService.loadUserByUsername("soohyun");

        //credentials는 사용자가 입력한 비밀번호
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, "123");

        //인증된 authentication입니다.
        Authentication authentication = authenticationManager.authenticate(token);

        //인증된 authentication를 SecurityHolder에 넣어줍니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //인증된 후에 dashboard를 호출
        sampleService.dashboard();
    }

    /*
    위의 코드를 간결하게 수정한 것
    @Test
	@WithMockUser
    public void dashboard() {
        sampleService.dashboard();
    }
     */

}