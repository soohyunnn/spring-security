package com.example.demo.form;

import com.example.demo.account.AccountContext;
import com.example.demo.account.AccountRepository;
import com.example.demo.account.UserAccount;
import com.example.demo.book.BookRepository;
import com.example.demo.common.CurrentUser;
import com.example.demo.common.SecurityLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.concurrent.Callable;

@Controller
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BookRepository bookRepository;
/*
    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if(principal == null){
            model.addAttribute("message", "Hello Spring Security");
        } else {
            model.addAttribute("message", "Hello " + principal.getName());
        }

        return "index";  //뷰의 이름(templates에 만든 파일 이름)
    }
*/
    @GetMapping("/")
    public String index(Model model, @CurrentUser UserAccount userAccount) {
        if(userAccount == null){
            model.addAttribute("message", "Hello Spring Security");
        } else {

            //userAccount.getAccount().getUsername();  //이런식으로도 Username에 접근 가능
            model.addAttribute("message", "Hello " + userAccount.getUsername());
        }

        return "index";  //뷰의 이름(templates에 만든 파일 이름)
    }

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("message", "Info");
        return "info";  //뷰의 이름(templates에 만든 파일 이름)
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("message", "Hello " + principal.getName());
        //AccountContext.setAccount(accountRepository.findByUsername(principal.getName()));
        sampleService.dashboard();
        return "dashboard";  //뷰의 이름(templates에 만든 파일 이름)
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("message", "Hello Admin" + principal.getName());
        return "admin";  //뷰의 이름(templates에 만든 파일 이름)
    }

    @GetMapping("/user")
    public String user(Model model, Principal principal) {
        model.addAttribute("message", "Hello User, " + principal.getName());
        System.out.println("aaa::" + principal);
        model.addAttribute("books", bookRepository.findCurrentUserBooks());
        return "user";
    }

    @GetMapping("/async-handler")
    @ResponseBody
    public Callable<String> asyncHandler() {
        SecurityLogger.log("MVC");

        //이렇게 return을 하면 Callable안의 로직을 처리하기 전에 이미 응답을 내보냅니다.
        //1.해당 request를 처리하고 있는 Thread를 반환(release)
        //2.Callable안의 로직이 완료가 됐을 때쯤 응답을 내보냅니다.
        return () -> {
                SecurityLogger.log("Callable");
                return "Async Handler";
        };
    }

    @GetMapping("/async-service")
    @ResponseBody
    public String asyncService() {
        SecurityLogger.log("MVC, before async service");
        sampleService.asyncService();
        SecurityLogger.log("MVC, after async service");
        return "Async Service";
    }


}
