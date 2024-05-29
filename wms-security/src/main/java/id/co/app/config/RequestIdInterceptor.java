package id.co.app.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class RequestIdInterceptor implements HandlerInterceptor {
    private static final String REQUEST_ID = "requestId";
    private static final String USERNAME = "username";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.put(REQUEST_ID, UUID.randomUUID().toString());

        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            if ("anonymousUser".equalsIgnoreCase(username)) {
                username = "";
            }
            MDC.put(USERNAME, username);
        } else {
            MDC.put(USERNAME, "anonymous");
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.remove(REQUEST_ID);
        MDC.remove(USERNAME);
    }
}
