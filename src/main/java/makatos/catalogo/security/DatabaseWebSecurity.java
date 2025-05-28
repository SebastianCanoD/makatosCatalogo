package makatos.catalogo.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {

	/**
	 * Configura el administrador de usuarios basado en JDBC, utilizando la tabla
	 * "usuarios". Se asume que los campos relevantes son: - usuario : nombre de
	 * usuario - contrasena : password (en este ejemplo sin codificar; en producción
	 * usar BCrypt o similar) - rol : autoridad de usuario (por ejemplo,
	 * "Administrador", "Empleado" o "Cliente")
	 *
	 * La consulta "usersByUsernameQuery" devuelve: - usuario, contrasena y, en este
	 * caso, siempre se asume "enabled" (true).
	 *
	 * La consulta "authoritiesByUsernameQuery" retorna la autoridad del usuario.
	 */
	@Bean
	public UserDetailsManager userDetailsManager(DataSource dataSource) {
		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

		// Consulta para obtener los datos del usuario: username, password y enabled (1
		// = true)
		users.setUsersByUsernameQuery("select usuario, contrasena, true as enabled from usuarios where usuario = ?");

		// Consulta para obtener la autoridad del usuario (se usa el campo 'rol')
		users.setAuthoritiesByUsernameQuery("SELECT usuario, rol AS authority FROM usuarios WHERE usuario = ?");

		return users;
	}

	/**
	 * Configuración del SecurityFilterChain. Aquí se definen las reglas de
	 * autorización según los endpoints, se especifica la página de login
	 * personalizada y se configuran las URLs de logout.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				// 1) Recursos estáticos (CSS, JS, imágenes...)
				.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

				// 2) Páginas públicas: home, login, error y registro de clientes
				.requestMatchers("/", "/home", "/login", "/error", "/cliente/registro", "/cliente/registro/**")
				.permitAll()

				// 3) Sólo ADMINISTRADOR para /usuario/**
				.requestMatchers("/usuario/**").hasAuthority("Administrador")

				// 4) Resto de la app: PRODUCTO, COMPRA, CLIENTE, EMPLEADO, REPORTE
				.requestMatchers("/producto/**", "/compra/**", "/cliente/**", "/empleado/**", "/reporte/**")
				.hasAnyAuthority("Administrador", "Empleado", "Cliente")

				// 5) Cualquier otra URL debe ir autenticada
				.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/perform_login")
						.defaultSuccessUrl("/home", true).permitAll())
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
						.logoutSuccessUrl("/login?logout").permitAll());

		return http.build();
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

}
