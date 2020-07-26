package com.juyoung.paycouponapi.config;

import com.juyoung.paycouponapi.filter.DocumentationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class FilterConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<DocumentationFilter> getFilterRegistrationBean() {
        FilterRegistrationBean<DocumentationFilter> registrationBean = new FilterRegistrationBean<>(new DocumentationFilter());
        registrationBean.setOrder(Integer.MIN_VALUE);
        registrationBean.setUrlPatterns(Arrays.asList("/", ""));  //루트 경로 호출 시 document 전달
        return registrationBean;
    }
}
