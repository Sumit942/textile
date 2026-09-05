package com.example.textile.filters;

import com.example.textile.entity.AuditLog;
import com.example.textile.service.AuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Autowired
    private AuditLogService auditLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request, response);
            response.copyBodyToResponse();
        }
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        AuditLog auditLog = new AuditLog();
        auditLog.setMethod(request.getMethod());
        auditLog.setUri(request.getRequestURI());
        auditLog.setQueryString(request.getQueryString());
        auditLog.setStatus(response.getStatus());

        auditLog.setRequestBody(getRequestBody(request));
        auditLog.setResponseBody(getResponseBody(response));
        auditLog.setUserName(getCurrentUserName());

        try {
            auditLogService.save(auditLog);
        } catch (Exception e) {
            log.error("Failed to save audit log", e);
        }

        if (log.isInfoEnabled()) {
            logRequestHeader(request, "[REQUEST]");
            logRequestBody(request, "[REQUEST BODY]");
            logResponse(response, "[RESPONSE]");
        }
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            return getContent(content, request.getCharacterEncoding());
        }
        return null;
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            return getContent(content, response.getCharacterEncoding());
        }
        return null;
    }

    private String getContent(byte[] content, String contentEncoding) {
        try {
            return new String(content, contentEncoding);
        } catch (UnsupportedEncodingException e) {
            return "[UNKNOWN ENCODING]";
        }
    }

    private String getCurrentUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            return ((org.springframework.security.core.userdetails.User) principal).getUsername();
        }
        return principal.toString();
    }

    private static void logRequestHeader(ContentCachingRequestWrapper request, String prefix) {
        String queryString = request.getQueryString();
        if (queryString == null) {
            log.info("{} {} {}", prefix, request.getMethod(), request.getRequestURI());
        } else {
            log.info("{} {} {}?{}", prefix, request.getMethod(), request.getRequestURI(), queryString);
        }
    }

    private static void logRequestBody(ContentCachingRequestWrapper request, String prefix) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, request.getCharacterEncoding(), prefix);
        } else {
            log.info("{} : [EMPTY]", prefix);
        }
    }

    private static void logResponse(ContentCachingResponseWrapper response, String prefix) {
        int status = response.getStatus();
        log.info("{} Status: {}", prefix, status);
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, response.getCharacterEncoding(), prefix + " BODY");
        }
    }

    private static void logContent(byte[] content, String contentEncoding, String prefix) {
        try {
            String payload = new String(content, contentEncoding);
            log.info("{} : {}", payload, prefix);
        } catch (UnsupportedEncodingException e) {
            log.info("{} : [UNKNOWN ENCODING]", prefix);
        }
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}
