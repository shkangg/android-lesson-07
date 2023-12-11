package kr.easw.lesson07.configurations;

import kr.easw.lesson07.Constants;
import kr.easw.lesson07.auth.JwtFilterChain;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SpringSecurityConfiguration {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final JwtFilterChain filterChain;

    @Bean
    @SneakyThrows
    SecurityFilterChain configureHttpSecurity(HttpSecurity security) {
        security
                .csrf(csrf -> csrf.disable())
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/dashboard").hasAnyAuthority(Constants.AUTHORITY_ADMIN, Constants.AUTHORITY_GUEST)
                            // /admin와 /management 엔드포인트에 대해, 관리자 권한을 가진 사용자만 접근할 수 있도록 설정합니다.
                            .requestMatchers("/admin", "/management").hasAnyAuthority(Constants.AUTHORITY_ADMIN)
                            // /api/v1/data/admin 엔드포인트에 대해, 관리자 권한을 가진 사용자만 접근할 수 있도록 설정합니다.
                            .requestMatchers("/api/v1/data/admin/**").hasAnyAuthority(Constants.AUTHORITY_ADMIN)
                            // /api/v1/data 엔드포인트에 대해, 관리자와 게스트 권한을 가진 사용자만 접근할 수 있도록 설정합니다.
                            .requestMatchers("/api/v1/data/**").hasAnyAuthority(Constants.AUTHORITY_ADMIN, Constants.AUTHORITY_GUEST)
                            
                            .requestMatchers("/api/v1/user/**").hasAnyAuthority(Constants.AUTHORITY_ADMIN)
                            .requestMatchers("/api/v1/auth/**").permitAll()
                            .anyRequest().permitAll()
                    ;
                })
                .logout(customizer -> {
                    customizer.logoutUrl("/logout");
                    customizer.logoutSuccessUrl("/?logout=true");
                })
                .formLogin(customizer -> {
                    customizer
                            .loginPage("/login")
                            .permitAll()
                            .defaultSuccessUrl("/dashboard")
                            .failureUrl("/login?error=true");
                })
                .addFilterBefore(filterChain, UsernamePasswordAuthenticationFilter.class)
        ;
        return security.build();
    }


    @Bean
    public BCryptPasswordEncoder encoder() {
        return encoder;
    }

}
