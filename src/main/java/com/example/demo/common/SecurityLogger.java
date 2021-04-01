package com.example.demo.common;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityLogger {

    public static void log(String message) {
        System.out.println(message);
        Thread thread = Thread.currentThread();  //어떤 Thread에서 가져오는지 찍기 위해 Thread를 가져옴
        System.out.println("Thread:: " +thread.getName());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Principal:: "+principal);
    }
}
