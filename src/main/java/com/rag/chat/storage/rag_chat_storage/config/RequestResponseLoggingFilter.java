package com.rag.chat.storage.rag_chat_storage.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        String path = request.getRequestURI();

        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey != null) apiKey = "***MASKED***";

        logger.info("Incoming Request: method={}, path={}, apiKey={}, remoteAddr={}",
                request.getMethod(), path, apiKey, request.getRemoteAddr());

        filterChain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;
        logger.info("Completed Request: path={}, status={}, duration={}ms",
                path, response.getStatus(), duration);
    }
}
