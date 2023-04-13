package edu.sru.thangiah.webrouting.services;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApiServiceImpl {
	
	 public String fetchLatitudeAndLongitude(String city, String state) {
	        String apiKey = "AIzaSyC9WwWWVbzUHeUBgj9AAh1aSMjSsrPpELM"; 
	        String result = "";
	        try {
	        	String encodedCity = URLEncoder.encode(city, "UTF-8");
	        	String encodedState = URLEncoder.encode(state, "UTF-8");
	        	
	            // Make HTTP request to Google Maps Geocoding API
	            String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
	                    encodedCity + "," + encodedState + "&key=" + apiKey;
	            URL url = new URL(urlString);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            int responseCode = connection.getResponseCode();

	            if (responseCode == HttpURLConnection.HTTP_OK) {
	                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	                String inputLine;
	                StringBuilder response = new StringBuilder();

	                while ((inputLine = in.readLine()) != null) {
	                    response.append(inputLine);
	                }
	                in.close();

	                
	                // Parse JSON response using Jackson ObjectMapper
	                ObjectMapper objectMapper = new ObjectMapper();
	                JsonNode jsonNode = objectMapper.readTree(response.toString());

	                if ("ZERO_RESULTS".equals(jsonNode.get("status").asText())) {
	                	return result;
	                }
	                
	                // Extract latitude and longitude from JSON response
	                if (jsonNode.has("results")) {
	                    JsonNode resultsNode = jsonNode.get("results");
	                    if (resultsNode.isArray() && resultsNode.size() > 0) {
	                        JsonNode firstResultNode = resultsNode.get(0);
	                        if (firstResultNode.has("geometry")) {
	                            JsonNode geometryNode = firstResultNode.get("geometry");
	                            if (geometryNode.has("location")) {
	                                JsonNode locationNode = geometryNode.get("location");
	                                String lat = padOrTrimString(locationNode.get("lat").asText().strip());
	                                String lng = padOrTrimString(locationNode.get("lng").asText().strip());
	                                result = lat + "|" + lng;
	                            }
	                        }
	                    }
	                }
	            } else {
	                System.out.println("HTTP request failed with response code: " + responseCode);
	                return result;
	            }

	        } catch (Exception e) {
	            return result;
	        }
			return result;
	    } 

	 public String padOrTrimString(String str) {
	        // If the input string is longer than the target length, trim it
		 	int targetLength = 9;
		 	
		 	if (str.startsWith("-")){
		 		++targetLength;
		 	}
	        if (str.length() > targetLength) {
	            return str.substring(0, targetLength);
	        }
	        // If the input string is shorter than the target length, pad it with zeros
	        else if (str.length() < targetLength) {
	            return String.format("%1$-" + targetLength + "s", str).replace(' ', '0');
	        }
	        // If the input string has the same length as the target length, return it as is
	        else {
	            return str;
	        }
	    }
	 
	 public String fetchDistanceBetweenCoordinates(String originLatitude, String originLongitude, String destinationLatitude, String destinationLongitude) {
		 
		 String apiKey = "AIzaSyC9WwWWVbzUHeUBgj9AAh1aSMjSsrPpELM"; 
	     String result = "";
	     
	     String urlString = "https://maps.googleapis.com/maps/api/directions/json?" +
	                "origin=" + originLatitude + "," + originLongitude +
	                "&destination=" + destinationLatitude + "," + destinationLongitude +
	                "&key=" + apiKey;

	        // Open a connection to the URL
	     
	     try {
	        URL url;
			url = new URL(urlString);

	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");

	        // Get the response from the API
	        int responseCode = connection.getResponseCode();
	        BufferedReader reader;
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	        	reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        } else {
	        	reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
	        }

	        // Read the response into a StringBuilder
	        StringBuilder response = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	        	response.append(line);
	        }
	        reader.close();

	        // Parse the JSON response using Jackson
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode root = objectMapper.readTree(response.toString());

	        // Extract the distance value from the JSON data
	        JsonNode routes = root.get("routes");
	        JsonNode firstRoute = routes.get(0);
	        JsonNode legs = firstRoute.get("legs");
	        JsonNode firstLeg = legs.get(0);
	        JsonNode distanceNode = firstLeg.get("distance");
	        result = distanceNode.get("text").asText();
	        
	        String [] parts = result.split(" ");
	        result = parts[0];

	     } catch (Exception e) {
	    	 return result;
	     }
	     return result;
	 } 

}
