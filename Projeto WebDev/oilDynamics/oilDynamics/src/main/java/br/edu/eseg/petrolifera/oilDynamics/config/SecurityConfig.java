package br.edu.eseg.petrolifera.oilDynamics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração mínima provisória. Sem ela o Spring Security bloqueia tudo, inclusive o CSS.
 * Na 3ª entrega (fase 9) aqui entram os perfis ADMIN e OPERADOR (RF-11 / RN-12).
 */
@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				// arquivos estáticos ficam públicos (a tela de login usa o CSS)
				.requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
				// console do H2: apenas para desenvolvimento
				.requestMatchers("/h2-console/**").permitAll()
				.anyRequest().authenticated())
			.formLogin(Customizer.withDefaults())
			.logout(Customizer.withDefaults())
			// o console do H2 não envia token CSRF e roda dentro de frames
			.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
			.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
		return http.build();
	}

}
