package edu.sru.thangiah.webrouting.captcha;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* Handles capturing the Captcha Response
* The variables deal with Google's Response JSON object
* @author Josh Gearhart 	jjg1018@sru.edu
* @since 9/6/2022
*/

 
public class RecieveCaptcha {
	
	@JsonProperty("success")
	private boolean success;
	
	@JsonProperty("challenge_ts")
	private String challenge_ts;
	
	@JsonProperty("hostname")
	private String hostname;
	
	@JsonProperty("error-codes")
	private String[] errorCodes;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getChallenge_ts() {
		return challenge_ts;
	}

	public void setChallenge_ts(String challenge_ts) {
		this.challenge_ts = challenge_ts;
	}

	public String getHostName() {
		return hostname;
	}

	public void setHostName(String hostName) {
		this.hostname = hostName;
	}

	public String[] getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(String[] errorCodes) {
		this.errorCodes = errorCodes;
	}


}