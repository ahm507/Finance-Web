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
import pf.user.UserEntity;
import pf.user.UserService;

import javax.sql.DataSource;
import java.util.logging.Logger;

@Configuration
@EnableWebSecurity

@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	
	 @Autowired
   UserService userService;
	
		private final static Logger LOGGER = Logger.getLogger(SecurityConfig.class.getName());

		
	@Override
	protected void configure(HttpSecurity http) throws Exception {


		//FIXME: remove "/rest/users/login.do", as it is no longer used
		http.authorizeRequests()
			.antMatchers("/**/*.js", "/**/*.html", "/**/*.css", "/**/*.png", "/**/*.jpg", 
					"/password-forget", "/register", "/contactus", "/login", "/rest/users/login.do", "/error", "/privacy", "/index", 
					"/", "/password-reset")
				.permitAll()
				.anyRequest().fullyAuthenticated()
		.and()
			.formLogin().loginPage("/login").failureUrl("/error").successForwardUrl("/transactions").
			defaultSuccessUrl("/transactions").permitAll()
		.and()
			.logout().invalidateHttpSession(true).permitAll()
				//TODO: enable CSRF protection
				.and().csrf().disable();
		
//		http.authorizeRequests().antMatchers("/css/**").permitAll().anyRequest()
//		.fullyAuthenticated().and().formLogin().loginPage("/login")
//		.failureUrl("/login?error").permitAll().and().logout().permitAll();
		
		
		
//		 .antMatchers("/transactions", "/accounts", "/export",
//		 "/exporting", "/settings", "/import",
//		 "/charts").fullyAuthenticated()
				
//				.successForwardUrl("/transactions")
//				.and().logout().permitAll();
				
//				.usernameParameter("email").passwordParameter("password")
//				.and().successForwardUrl("/transactions")
//				.defaultSuccessUrl("/transactions")
				
//				.and()
//				.exceptionHandling().accessDeniedPage("/403")
//				.and().logout().logoutUrl("/logout")
//				.logoutSuccessUrl("/login?logout");

	}
	
	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		 auth
//		 .jdbcAuthentication()
//		 .dataSource(dataSource)
//		 .usersByUsernameQuery("select username, password, enabled from users where username=?")
//		 .authoritiesByUsernameQuery("select email, 'USER' from user where email=?");
//		 .withUser("user").password("password").roles("USER");


		
		auth.authenticationProvider(new AuthenticationProvider() {
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				String email = (String) authentication.getPrincipal();
				String providedPassword = (String) authentication.getCredentials();
				UserEntity user = null;
				try {
					user = userService.login(email, providedPassword);
				} catch( Exception ex) {
					throw new BadCredentialsException(
							"Username/Password does not match for " + authentication.getPrincipal(), ex);
				}
				
				LOGGER.info("Logged in user " + user.getEmail());
				
				return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			}
			
			@Override
			public boolean supports(Class<?> authentication) {
				return true;
			}

			
		});
	
		auth.jdbcAuthentication().dataSource(this.dataSource);
		//TODO: Use ready made BCryptPasswordEncoder, but must updated register and login services
		//.passwordEncoder(new BCryptPasswordEncoder()); //

	
	
	}


}

