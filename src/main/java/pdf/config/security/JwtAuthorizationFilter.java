package pdf.config.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import pdf.utils.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private TokenUtil tokenUtil;

    public JwtAuthorizationFilter(TokenUtil tokenUtil) {

        this.tokenUtil = tokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {

            String token = bearerToken.replace("Bearer ", "");

            UsernamePasswordAuthenticationToken usernamePAT = tokenUtil.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(usernamePAT);

        }

        filterChain.doFilter(request, response);

    }

}