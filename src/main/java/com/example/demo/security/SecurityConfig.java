package com.example.demo.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityExpressionHandler expressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);

        return handler;
    }

    //여기에서는 동적으로 처리되는 리소스를 처리하는 것은 좋지 않습니다. HttpSecurity를 통해 처리하는 것을 추천!!
    //=> 동적 페이지는 인증된 사용자가 있는 경우, 없는 경우에도 접속할 수 있기 때문에 FilterChainProxy의 Filter들을 타야합니다.
    //=> 그래야지만 현재 로그인된 사용자의 정보를 SecurityContextHolder에 넣어주고 할 수 있습니다.
    //=> 정적인 리소스를 처리할 때만 사용하는 것이 좋습니다.
    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring().mvcMatchers("/favicon.ico");
        //위와 같은 기능을 수행하는 스프링이 제공하는 것
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")  //모든 요청을 다 처리할 것데
                .authorizeRequests()  //요청을 어떻게 인가할지 아래에 설정하는 과정이다. (http.authorizeRequests() : 요청의 인가 설정은...)
                .mvcMatchers("/", "/info", "/account/**").permitAll()  //루트 경로는 모든 사용자 접근 가능
                .mvcMatchers("/admin").hasRole("ADMIN")  // '/admin' 경로는 권한이 ADMIN인 사용자만 접근 가능
                .mvcMatchers("/user").hasRole("USER")   //'/user' 경로는 권한이 USER인 사용자만 접근 가능
                .anyRequest().authenticated()    //anyRequest()는 기타 등등을 의미, 기타 등등에 대한 접근은 인증만 하면 가능
                .expressionHandler(expressionHandler());

        http.formLogin()  //그리고 formLogin 사용
                .and()
                .httpBasic(); //그리고 httoBasic 사용

    }
}