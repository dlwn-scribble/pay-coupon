package com.juyoung.paycouponapi.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DocumentationFilter implements Filter {

    private static String DOCUMENTATION_URL = "/swagger-ui.html";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.sendRedirect(DOCUMENTATION_URL);
    }
}
