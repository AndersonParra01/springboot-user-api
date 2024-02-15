package pdf.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class TokenUtil {

    private SecretKey secretKey;

    public TokenUtil(SecretKey secret) throws Exception {

        this.secretKey = secret;

    }

    private final static long expiration = 121212121l;

    public String createToken(Map<String, ?> claims) {

        Date dateExpiration = new Date(System.currentTimeMillis() + expiration);
        System.out.println();
        String token = Jwts
                .builder()
                .claims(claims)
                .expiration(dateExpiration).expiration(dateExpiration)
                .signWith(secretKey).compact();

        return token;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {

        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

        String subject = (String) claims.get("subject");

        String role = (String) claims.get("role");

        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(role));

        return new UsernamePasswordAuthenticationToken(subject, null, authorities);

    }

    public String getSubject(String token) {

        if (token.contains("Bearer"))
            token = token.split(" ")[1];

        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

        String subject = (String) claims.get("subject");

        return subject;
    }

}
