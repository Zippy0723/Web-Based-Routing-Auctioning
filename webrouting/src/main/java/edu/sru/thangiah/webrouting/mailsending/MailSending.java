package edu.sru.thangiah.webrouting.mailsending;

import javax.servlet.http.HttpServletRequest;

/**
 * Used to request the url for mail sending
 * @author Josh Gearhart	jjg1018@sru.edu
 * @since 9/21/2022
 */
public class MailSending {
	
	
	/**Used to create a context path for the email
	 * @param Servlet request handles requesting the url to throw into the string url.
	 * @return a servlet path that replaces the set string
	 */
	
public static  String getUrl(HttpServletRequest request) {
	String url;
	url = request.getRequestURL().toString();
	return url.replace(request.getServletPath(), "");
	}
}
