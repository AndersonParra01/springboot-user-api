package pdf.config.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import pdf.utils.Encryptor;
import pdf.utils.TokenUtil;
import static pdf.utils.PropertiesConverter.JWT_RANDOM_KEY;
import static pdf.utils.PropertiesConverter.SECRET_KEY;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtEntryPoint jwtEntryPoint;

    @Value(SECRET_KEY)
    private SecretKey secretKey;

    @Value(JWT_RANDOM_KEY)
    private SecretKey jwtSecretKey;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {

        TokenUtil tokenUtil = new TokenUtil(jwtSecretKey);

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenUtil);
        jwtAuthenticationFilter.setAuthenticationManager(authManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");

        http.csrf((c) -> c.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/auth/login")
                        .permitAll()
                        .requestMatchers("/subscriptios-plan")
                        .permitAll()
                        .requestMatchers("/auth/register")
                        .permitAll()
                        .requestMatchers("/roles")
                        .permitAll()
                        .requestMatchers("/users/create")
                        .permitAll()
                        .requestMatchers("/roles/create")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(new JwtAuthorizationFilter(tokenUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exh -> exh.authenticationEntryPoint(jwtEntryPoint))
                .cors((cors) -> cors.configure(http));

        return http.build();

    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
        return builder.build();

    }

    @Bean
    PasswordEncoder encoder() throws InvalidKeySpecException, NoSuchAlgorithmException {
        return new Encryptor(secretKey);
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

    @Bean
    SecretKey jwSecretKey() {

        return this.jwtSecretKey;

    }

}
