package com.example.demo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//GenericFilterBean 상속받아 구현하면 doFilter 하나만 구현하면 되서 간단하다.
public class LogginFilter extends GenericFilterBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(((HttpServletRequest)request).getRequestURI());  //URI이름이 task 이름이 됨.

        filterChain.doFilter(request, response);  //이렇게 해야 다음 filter로 넘어감.

        stopWatch.stop();
        logger.info(stopWatch.prettyPrint());
    }
}
