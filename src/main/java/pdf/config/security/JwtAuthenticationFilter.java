package pdf.config.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import pdf.persistence.Token;
import pdf.services.UserService;
import pdf.utils.Json;
import pdf.utils.TokenUtil;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    UserService service;

    private TokenUtil tokenUtil;

    public JwtAuthenticationFilter(TokenUtil tokenUtil) {

        this.tokenUtil = tokenUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        Auth auth = new Auth();

        try {

            auth = new ObjectMapper().readValue(request.getReader(), Auth.class);

        } catch (Exception ex) {

        }

        UsernamePasswordAuthenticationToken usernamePAT = new UsernamePasswordAuthenticationToken(
                auth.getUsername(), auth.getPassword(), Collections.emptyList());

        return getAuthenticationManager().authenticate(usernamePAT);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws java.io.IOException, ServletException {

        UserDetailsImpl user = (UserDetailsImpl) authResult.getPrincipal();

        String token = tokenUtil.createToken(user.claims());

        response.setContentType("application/json");

        // Obtener rol
        List<String> roles = user.getAuthorities().stream()
                .map((authority) -> authority.getAuthority()).toList();

        // Crear body de respuesta
        Token body = new Token(token, String.join(",", roles));

        // Escribir el body en la respuesta
        try (java.io.PrintWriter out = response.getWriter()) {

            out.write(Json.convertirObjetoToJson(body));

        } catch (IOException e) {
            e.printStackTrace();
        }

        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
