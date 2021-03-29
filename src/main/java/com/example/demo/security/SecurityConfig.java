package com.example.demo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()  //요청을 어떻게 인가할지 아래에 설정하는 과정이다. (http.authorizeRequests() : 요청의 인가 설정은...)
                .mvcMatchers("/", "/info", "/account/**").permitAll()  //루트 경로는 모든 사용자 접근 가능
                .mvcMatchers("/admin").hasRole("ADMIN")  // '/admin' 경로는 권한이 ADMIN인 사용자만 접근 가능
                .anyRequest().authenticated()    //anyRequest()는 기타 등등을 의미, 기타 등등에 대한 접근은 인증만 하면 가능
                .and()
                .formLogin()  //그리고 formLogin 사용
                .and()
                .httpBasic(); //그리고 httoBasic 사용

    }



}