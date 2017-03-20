package pf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import pf.user.UserEntity;
import pf.user.UserRepository;
import pf.user.UserService;

import javax.sql.DataSource;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

@Configuration
@EnableWebSecurity

@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	 UserService userService;
	 public SecurityConfig(UserService userService) {
		 this.userService = userService;
	}	 
	
	private final static Logger LOGGER = Logger.getLogger(SecurityConfig.class.getName());

		
	@Override
	protected void configure(HttpSecurity http) throws Exception {


		//FIXME: remove "/rest/users/login.do", as it is no longer used
		http.authorizeRequests()
			.antMatchers("/**/*.js", "/**/*.html", "/**/*.css", "/**/*.png", "/**/*.jpg", 
					"/password-forget", "/register", "/contactus", "/login", "/rest/users/login.do", "/error", "/error2", "/privacy", "/index", 
					"/", "/password-reset")
				.permitAll()
				.antMatchers("/admin").hasRole("ADMIN") //No admn interface yet!
				.antMatchers("/transactions", "/accounts", "/charts", "/export", "/import", "/settings").hasRole("USER")
				.antMatchers("/rest/**").hasRole("USER")
				.anyRequest().fullyAuthenticated() 
		.and()
			.formLogin().loginPage("/login").failureUrl("/login?msg=error").successForwardUrl("/transactions")
				.defaultSuccessUrl("/transactions").permitAll()
		.and()
			.logout().logoutSuccessUrl("/login?msg=logout").invalidateHttpSession(true).permitAll()
				//TODO: enable CSRF protection
				.and().csrf().disable();
		
	}
	
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		
//		auth.jdbcAuthentication()
//				.usersByUsernameQuery("select email,password,verified from user where email = ?")
//				.authoritiesByUsernameQuery("select username, authority from authorities where username = ?")
//				.passwordEncoder(new PasswordEncoder() {
//
//					@Override
//					public String encode(CharSequence rawPassword) {
//						try {
//							return UserService.md5(rawPassword.toString());
//						} catch (NoSuchAlgorithmException e) {
//							LOGGER.severe("NoSuchAlgorithmException: MD5 algorithim does not exist");
//							e.printStackTrace();
//						}
//						return rawPassword.toString();						
//					}
//
//					@Override
//					public boolean matches(CharSequence rawPassword, String encodedPassword) {
//						return encode(rawPassword).equals(encodedPassword);
//					}
//					
//				});
			
		auth.authenticationProvider(new AuthenticationProvider() {
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				String email = (String) authentication.getPrincipal();
				String providedPassword = (String) authentication.getCredentials();
				UserEntity user = null;
				try {
					user = userService.login(email, providedPassword);
				} catch( Exception ex) { //throwing exception is the only way to show user name/password is incorrect.
					throw new BadCredentialsException(
							"Username and/or Password is not correct.", ex);
				}
				
				LOGGER.info("Logged in user " + user.getEmail());
				
				return new UsernamePasswordAuthenticationToken(email, providedPassword, user.getAuthorities());
//				return new UsernamePasswordAuthenticationToken(email, providedPassword);
			}
			
			@Override
			public boolean supports(Class<?> authentication) {
				return true;
			}

			
		});
	
		

		
		
	}
	
	
}

