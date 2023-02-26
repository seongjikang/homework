package com.ably.api.userapi.config;

import com.ably.api.userapi.filter.AuthorizationHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    Environment env;

    @Autowired
    public WebConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public FilterRegistrationBean filterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new AuthorizationHeaderFilter(env));
        registrationBean.setOrder(Integer.MIN_VALUE);
        registrationBean.addUrlPatterns("/users/*");

        return registrationBean;
    }
}
