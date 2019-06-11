package com.example.clientversion1.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(urlPatterns = {"/mappings","/metrics","/auditevents"})
public class ActuatorFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ResponseWrapper hsrw = new ResponseWrapper(response);

        filterChain.doFilter(request,hsrw);

        servletResponse.setContentType("application/vnd.spring-boot.actuator.v2+json;charset=UTF-8");
        PrintWriter pw = servletResponse.getWriter();
        pw.write(hsrw.getData());
        pw.flush();
        pw.close();
    }

    @Override
    public void destroy() {

    }
}
