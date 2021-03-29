package com.example.demo.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Test
    @WithAnonymousUser
    public void index_anonymous() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUser
    public void index_user() throws Exception {
        //soohyun이라는 유저가 데이터 베이스에 있다가 아니라는 점을 주의!!
        //soohyun이라는 가짜 유저가 해당 뷰를 보면 어떻게 될 것인가 라는 TEST(soohyun유저가 로그인 된 상태일 때 Index 페이지를 요청하면 어떻게 될 것인가...의 TEST)
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUser
    public void admin_user() throws Exception {
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void admin_admin() throws Exception {
        mockMvc.perform(get("/admin").with(user("admin").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //formLogin() TEST => .formLogin()은 커스텀 가능(loginPage로 가는 Url 변경, username을 명시하는 파라미터 이름을 변경, 성공했을 때 이동하는 페이지를 변경 등등...)
    //TEST에서 DB에 변경사항이 있을 경우 @Transactional을 추가하는 것이 좋다.
    @Test
    @Transactional
    public void login_success() throws Exception {
        String username = "soohyun";
        String password = "123";
        Account user = this.createUser(username, password);
        mockMvc.perform(formLogin().user(user.getUsername()).password(password))  //formLogin을 soohyun의 user의 password 123으로 접속을 하면 => 123 대신 user.getPassword() 를 쓰면 안됨. WHY? encodePassword로 바꿨기 때문에 바꾼값이 불러와지기 때문에 오류 발생
                .andExpect(authenticated());  //로그인 결과 인증이 되어있는지 TEST
    }

    @Test
    @Transactional
    public void login_fail() throws Exception {
        String username = "soohyun";
        String password = "123";
        Account user = this.createUser(username, password);
        mockMvc.perform(formLogin().user(user.getUsername()).password("12345"))  //formLogin을 soohyun의 user의 password 123으로 접속을 하면 => 123 대신 user.getPassword() 를 쓰면 안됨. WHY? encodePassword로 바꿨기 때문에 바꾼값이 불러와지기 때문에 오류 발생
                .andExpect(unauthenticated());  //로그인 결과 인증이 실패 되어있는지 TEST
    }

    private Account createUser(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setRole("USER");
        accountService.createNew(account);
        return account;
    }


}