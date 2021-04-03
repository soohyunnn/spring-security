package com.example.demo.form;

import com.example.demo.account.Account;
import com.example.demo.account.AccountContext;
import com.example.demo.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SampleService {

//    public void dashboard() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        //사용자의 정보
//        Object principal = authentication.getPrincipal();//authentication 안에 principal이 존재함.(Object 타입)보
//        //사용자의 권한정보 => GrantAuthority에 Collection이 들어있음.
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        //인증을 한 다음에는 credentials을 가지고 있을 필요가 없음
//        Object credentials = authentication.getCredentials();
//        //인증된 사용자인지 확인
//        boolean authenticated = authentication.isAuthenticated();
//    }

        @Secured("ROLE_USER")
        public void dashboard() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("=================");
            System.out.println(userDetails.getUsername());
        }

        //@Async : 특정 빈 안에 있는 메소드를 호출할 때 별도의 Thread를 만들어서 비동기적으로 호출해주는 어노테이션
        @Async
        public void asyncService() {
            SecurityLogger.log("Async Service");
            System.out.println("Async service is called.");
        }
}
