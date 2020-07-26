package com.juyoung.paycouponapi.filter;

import com.juyoung.paycouponapi.security.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    public AuthenticationFilter(TokenProvider provider){
        this.tokenProvider = provider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = tokenProvider.resolveToken((HttpServletRequest) request);
        if(token!=null && !token.isEmpty() && tokenProvider.validateToken(token))
        {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication); //인증 추가
        }

        chain.doFilter(request, response); //다음 체인 체인 호출
    }
}
