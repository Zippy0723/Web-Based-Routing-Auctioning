package edu.sru.thangiah.webrouting.web;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * Extends the SimpleUrlAuthenticationSuccessHandler to log interaction
 * @author Dakota Myers drm1022@sru.edu
 * @since 1/01/2023
 */

public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	
	private static final Logger Logger = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);
	
	/**
	 * This extends the springboot AuthenticationSuccess to log who logged in
	 */
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
       
        Logger.info("{} || logged in.", authentication.getName());
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
          setDefaultTargetUrl("/users");
        } else if (authorities.contains(new SimpleGrantedAuthority("SHIPPER"))) {
          setDefaultTargetUrl("/allshipments");
        } 
        else if (authorities.contains(new SimpleGrantedAuthority("CARRIER"))) {
            setDefaultTargetUrl("/allshipments");
        }
        else if (authorities.contains(new SimpleGrantedAuthority("SHADOWADMIN"))) {
            setDefaultTargetUrl("/loghome");
        }
        else if (authorities.contains(new SimpleGrantedAuthority("MASTERLIST"))) {
            setDefaultTargetUrl("/allshipments");
        }
        else 
        {
          setDefaultTargetUrl("/");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
	
	

}
