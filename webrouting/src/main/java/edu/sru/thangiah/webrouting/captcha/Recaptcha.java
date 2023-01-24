package edu.sru.thangiah.webrouting.captcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Used for verifying the recaptcha
 * @author Josh Gearhart	jjg1018@sru.edu
 * @Since 9/6/2022
 */
@Component
public class Recaptcha {
	
    @Value("https://www.google.com/recaptcha/api/siteverify")
	private String url;

	@Value("${google.recaptcha.key.secret}")
	private String secret;
	
	@Autowired
    private RestTemplate restTemplate;
    

    private HttpHeaders headers;
    
   
    private RecieveCaptcha response;
    

    private MultiValueMap<String, String> map;
    

    private HttpEntity<MultiValueMap<String,String>> request;
    
   
    /**
     * Creates a boolean for verifying the recaptcha
     * adds the secret key and the response to a request that is sent to google, the response takes 
     * in googles response this is then used to print out the JSON of the captcha
     * @param googleResponse which is used to request the response from the captcha
     * @return response.isSuccess
     */
	public boolean VerifyRecaptcha(String googleResponse) {
	    	headers = new HttpHeaders();
	    	map = new LinkedMultiValueMap<>();
	    	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    	map.add("secret", secret);
	    	map.add("response", googleResponse);
	    	request = new HttpEntity<>(map, headers);
	    	response = restTemplate.postForObject(url, request, RecieveCaptcha.class);
	    	//System.out.println("Success: "+ response.isSuccess());
	    	//System.out.println("Challenge Timestamp: "+ response.getChallenge_ts());
	    	//System.out.println("hostname: "+ response.getHostName()); 
	    	//if(response.getErrorCodes()!= null) {
	    	//	for(String error : response.getErrorCodes()) {
	    		//	System.out.println("Error Codes: "+ error);
	    		//}
	    	//} 
	    	//@TODO Log this.
	    	return response.isSuccess();
	    }
	
	
}
