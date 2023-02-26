package com.ably.api.userapi.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/users/*")
public class AuthorizationHeaderFilter implements Filter {

    Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        this.env = env;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if(req.getHeader("Authorization") == null) {
            throw new RuntimeException("no authorization header");
        }

        String authorizationHeader = req.getHeader("Authorization");
        String jwt = authorizationHeader.replace("Bearer ", "");

        if(!isJwtValid(jwt)) {
            throw new RuntimeException("login token is not valid");
        }

        chain.doFilter(request, response);


    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject =null;

        try {
            subject = Jwts.parserBuilder().setSigningKey(env.getProperty("login_token.secret"))
                    .build().parseClaimsJws(jwt).getBody().getSubject();
        } catch (Exception e) {
            returnValue = false;
        }

        if(subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;

    }
}
