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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
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
                .mvcMatchers("/", "/info", "/account/**", "/signup").permitAll()  //루트 경로는 모든 사용자 접근 가능
                .mvcMatchers("/admin").hasRole("ADMIN")  // '/admin' 경로는 권한이 ADMIN인 사용자만 접근 가능
                .mvcMatchers("/user").hasRole("USER")   //'/user' 경로는 권한이 USER인 사용자만 접근 가능
                .anyRequest().authenticated()    //anyRequest()는 기타 등등을 의미, 기타 등등에 대한 접근은 인증만 하면 가능
                .expressionHandler(expressionHandler());

        http.formLogin()  //그리고 formLogin 사용
//                .usernameParameter("my-username")
//                .passwordParameter("my-password")
//                .failureForwardUrl()  //로그인 실패시 이동할 url
//                .successForwardUrl()  //로그인 성공시 이동할 url
                .loginPage("/login").permitAll();  //로그인 페이지를 따로 설정(DefaultLogin/LogoutPageGeneratingFilter를 제공하지 X => 직접 구현해야 함)

        http.httpBasic(); //그리고 httoBasic 사용

        /*
        http.sessionManagement()
                .sessionFixation()
                    .changeSessionId()
                //.none()  //세션 변조를 사용하지 않겠다는 의미
                //.newSession()  //세션을 새로 만들겠다는 의미
                //.invalidSessionUrl("/login"); //유효하지 않은 세션이 접근했을 때 어디로 보낼것인가?
                .maximumSessions(1)
                    .maxSessionsPreventsLogin(true);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        */
        http.logout().logoutSuccessUrl("/");
//                .logoutUrl("/logout")  //logout을 처리하는 url(기본으로 제공해주는 로그아웃 페이지가 /logout 임. 다른 url로 바꾸려면 새로 커스터마이징 한 페이지를 생성해야 합니다.
//                  //logout이 성공하면 이동할 url
//                .invalidateHttpSession(true)  //로그아웃 한 후 HttpSession을 invalid 처리 할 것인가(기본값이 true)
//                .deleteCookies();  //쿠키 기반의 인증 방법을 사용할 경우 로그아웃 한 후 쿠키를 삭제해주는 역할을 함.



        //기본값은 동일한 Thread내에서만 공유됨.
        //현재 Thread에서 하위 Thread로 SecurityContext가 공유가 됨.
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

    }
}