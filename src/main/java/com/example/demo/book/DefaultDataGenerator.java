package com.example.demo.book;

import com.example.demo.account.Account;
import com.example.demo.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultDataGenerator implements ApplicationRunner {

    @Autowired
    AccountService accountService;

    @Autowired
    BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //User 2명 생성(soohyun - spring, yeoungsub - hibernate)
        Account soohyun = createUser("soohyun");
        Account yeoungsub = createUser("yeoungsub");

        createBook("spring",soohyun);
        createBook("hibernate", yeoungsub);
    }

    private void createBook(String title, Account author) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        bookRepository.save(book);
    }

    private Account createUser(String username) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword("123");
        account.setRole("_USER");
        return accountService.createNew(account);
    }
}
