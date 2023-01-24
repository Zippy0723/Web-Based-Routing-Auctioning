package edu.sru.thangiah.webrouting.error;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * Logs when a user accesses a page without permission
 * Also redirects to the 403 access denied page (403.html)
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/7/2022
 */

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
	
	private static Logger logger = LoggerFactory.getLogger(MyAccessDeniedHandler.class);
	
	/**
	 * Redirects the user to the 403 page if the user does not have access to the page
	 * @param httpServletRequest Used as the http server request
	 * @param httpServletResponse Used as the http server response
	 * @param e Access denied exception
	 * @throws IOException Exception thrown if there is an error
	 * @throws ServletException Exception thrown if there is an error
	 */
	@Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {

        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            logger.info("User '" + auth.getName()
                    + "' attempted to access the protected URL: "
                    + httpServletRequest.getRequestURI());
        }
        else {
        	httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/403");
        }
    }
        
}
