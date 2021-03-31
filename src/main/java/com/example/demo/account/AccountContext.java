package com.example.demo.account;

public class AccountContext {

    private static final ThreadLocal<Account> ACCOUNT_THREAD_LOCAL = new ThreadLocal<>();  //Account를 저장할 수 있는 THREAD 하나 생성

    //THREAD안에 Account객체 저장
    public static void setAccount(Account account) {
        ACCOUNT_THREAD_LOCAL.set(account);
    }

    //THREAD에서 Account 객체 꺼내오기
    public static Account getAccount() {
        return ACCOUNT_THREAD_LOCAL.get();
    }

}