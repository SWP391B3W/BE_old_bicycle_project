package swp391.old_bicycle_project.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.security.JwtTokenProvider;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class WebSocketSecurityInterceptor implements ChannelInterceptor {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebSocketSecurityInterceptor.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    if (jwtTokenProvider.validateToken(token)) {
                        String email = jwtTokenProvider.extractEmail(token);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                        if (userDetails instanceof User) {
                            User user = (User) userDetails;
                            // Set custom principal where getName() returns the userId string
                            // This is crucial for convertAndSendToUser(userId.toString(), ...)
                            StompPrincipal principal = new StompPrincipal(user.getId().toString());
                            
                            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                    user, null, user.getAuthorities());
                            
                            accessor.setUser(new AuthenticatedPrincipalWrapper(principal, auth));
                            log.info("WebSocket connected for user: {}", user.getId());
                        }
                    }
                } catch (Exception e) {
                    log.error("WebSocket authentication failed: {}", e.getMessage());
                }
            }
        }
        return message;
    }

    private static class StompPrincipal implements Principal {
        private final String name;
        public StompPrincipal(String name) { this.name = name; }
        @Override
        public String getName() { return name; }
    }

    private static class AuthenticatedPrincipalWrapper extends UsernamePasswordAuthenticationToken implements Principal {
        private final Principal stompPrincipal;

        public AuthenticatedPrincipalWrapper(Principal stompPrincipal, UsernamePasswordAuthenticationToken auth) {
            super(auth.getPrincipal(), auth.getCredentials(), auth.getAuthorities());
            this.stompPrincipal = stompPrincipal;
        }

        @Override
        public String getName() {
            return stompPrincipal.getName();
        }
    }
}
