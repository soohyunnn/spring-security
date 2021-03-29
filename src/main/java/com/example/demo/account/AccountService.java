package com.example.demo.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        //user정보가 없을 경우
        if(account == null) {
            throw new UsernameNotFoundException(username);
        }

        //user정보가 있을 경우 UserDetails 타입으로 바꿔줘야 함.
        //즉, Account -> UserDetails 으로 바꿔줘야 함. 이러한 과정을 편리하게 하도록 SpringSecurity가 User라는 클래스를 제공해줌.
        //builder()를 사용해서 build()를 하면 User객체의 타입을 만들수 있음.
        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public Account createNew(Account account) {
        //account.setPassword("{noop}" + account.getPassword());
        account.encodePassword(passwordEncoder);
        return this.accountRepository.save(account);
    }
}
