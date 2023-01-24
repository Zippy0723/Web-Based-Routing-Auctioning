package edu.sru.thangiah.webrouting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import edu.sru.thangiah.webrouting.web.UserValidator;

/**
 * Sets up the Spring Security with Thymeleaf. <br>
 * Redirects user to login unless page is allowed to a user. <br>
 * Handles user authentication.
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/7/2022
 */

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	
	@Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

	/**
	 * Used to get an instance of the UserValidator class
	 * @return new user validator instance
	 */
    @Bean
    public UserValidator userValidator() {
        return new UserValidator(null);
    }
	
    /**
     * Handles all authentication for different roles and authorities. <br>
     * Sets up which pages the user has access to by default. <br>
     * Sets up Manager, Assistant Manager, Operations Manager, and Admin authorities.
     * @param http Handles http security
     * @throws Exception
     */
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
        http.csrf().disable()
                .authorizeRequests()
                	
					.antMatchers("/", "/index", "/registrationhome", "/styles/**", "/h2-console/**", "/error", "/403", "/registrationshipper", "/registrationcarrier").permitAll()
					
					.antMatchers("/contacts", "/add/add-contact", "/signupcontact", "/addcontact", 
							"/deletecontact/**", "/editcontact/**", "/updatecontact/**").hasAnyAuthority("CARRIER")
					
					.antMatchers("/drivers", "/add/add-driver", "/add-driver"," /adddriver",
							"/deletedriver/**", "/editdriver/**", "/updatedriver/**").hasAnyAuthority("CARRIER")
					
					.antMatchers("/locations","/add/add-location","/add-location","/addlocations",
							"/deletelocations/**","editlocations/**","/updatelocation/**").hasAnyAuthority("CARRIER")
					
					.antMatchers("/maintenanceorders","/add/add-maintenance","/add-maintenance","/addmaintenance",
							"/deleteorder/**","/editorder/**","/updateorder/**").hasAnyAuthority("CARRIER")
					
					.antMatchers("/routes","/add/add-routes","/add-routes","/addroutes",
							"/deleteroutes/**","/editroutes/**","/updateroutes/**").hasAnyAuthority("CARRIER","MASTERLIST")
					
					.antMatchers("/add/add-shipments","/add-shipments","/addshipments",
							"/deleteshipment/**").hasAnyAuthority("SHIPPER","MASTERLIST")
					
					.antMatchers("/editshipment/**","/updateshipment/**").hasAnyAuthority("CARRIER", "SHIPPER")
					
					.antMatchers("/update/update-shipments").hasAnyAuthority("CARRIER","MASTERLIST")
					
					.antMatchers("/update/update-shipments-shipper").hasAuthority("SHIPPER")
					
					.antMatchers("/shipments").hasAuthority("MASTERLIST")
					
					.antMatchers("/shipmentshomecarrier").hasAuthority("CARRIER")
					
					.antMatchers("/shipmentshomeshipper").hasAuthority("SHIPPER")
					
					.antMatchers("/carriers").hasAnyAuthority("CARRIER","MASTERLIST")
					
					.antMatchers("/editcarrier/**","/updatecarrier/**").hasAnyAuthority("CARRIER")
					
					.antMatchers("/technicians","/add/add-technician","/add-technician","/addtechnician",
							"/deletetechnician/**","/edittechnician/**","/updatetechnician/**").hasAnyAuthority("CARRIER")
					
					.antMatchers("/vehicles","/add/add-vehicle","/add-vehicle","/addvehicles",
							"/deletevehicles/**","/editvehicles/**","/updatevehicle/**").hasAnyAuthority("CARRIER")
					
					.antMatchers("/vehicletypes","/add/add-vehicletype","/signupvehicletype","/addvehicletypes",
							"/deletevehicletype/**","/editvehicletype/**","/updatevehicletype/**").hasAnyAuthority("CARRIER")
					
					.antMatchers("/users","/roles").hasAnyAuthority("ADMIN")
					
					//.antMatchers("/**").hasAnyAuthority("ADMIN", "CARRIER", "SHIPPER", "MASTERLIST")
					.antMatchers("/verify","/verified", "/verificationfail","/forgotpasswordform","/forgotpassword", "/resetpassword", "/resetpasswordform").permitAll()
					.anyRequest().authenticated()
					
					
                .and()
                .formLogin()
					.loginPage("/login")
					.permitAll()
					//.failureUrl("/login?error=true")
					.and()
                .logout()
					.permitAll()
					.and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);

    }
	
	/**
	 * Used to get the authentication manager class
	 * @return authenticationManager
	 * @throws Exception Throws exception if there is an error
	 */
	@Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

	/**
	 * Sets up how a user is authenticated and how the password is encoded
	 * @param auth From the AuthenticationManagerBuilder
	 * @throws Exception Throws exception if there is an error
	 */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
