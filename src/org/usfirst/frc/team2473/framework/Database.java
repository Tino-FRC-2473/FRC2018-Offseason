package org.usfirst.frc.team2473.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 *
 */
public class Database {
	private Map<String, Double> numerical_values; //hashmap stores the double values under a String key
	private Map<String, Boolean> conditional_values; //hashmap stores boolean values under String key
	private Map<String, String> message_values; //hashmap stores String values under a String key

	private static Database theInstance; //static reference

	static {
		theInstance = new Database(); //creating the static reference
	}
	
	private Database() { //constructor is purposefully private; no creating object
		numerical_values = new HashMap<>();
		conditional_values = new HashMap<>();
		message_values = new HashMap<>();
	}
	
	public static Database getInstance() { //returns static reference
		return theInstance;
	}
		
	/**
	 * @param key String key under which to store the value
	 * @param value new double value
	 * */
	public void setNumeric(String key, double value) {
		numerical_values.put(key, value);
	}
		
	/**
	 * @param key the String value of the key
	 * @return the double value under the key
	 * */
	public double getNumeric(String key) {
		return numerical_values.get(key).doubleValue();
	}
	
	/**
	 * @return all the numerical values in a map
	 * */
	public Map<String, Double> numericalEntries() {
		return numerical_values;
	}

	/**
	 * @param key String key under which to store the value
	 * @param value new boolean value
	 * */
	public void setConditional(String key, boolean conditional) {
		conditional_values.put(key, conditional);
	}

	/**
	 * @param key the String value of the key
	 * @return the boolean value under the key
	 * */
	public boolean getConditional(String key) {
		return conditional_values.get(key).booleanValue();
	}
	
	/**
	 * @return all the boolean values in a map
	 * */
	public Map<String, Boolean> conditionalEntries() {
		return conditional_values;
	}

	/**
	 * @param key String key under which to store the value
	 * @param value new String value
	 * */
	public void setMessage(String key, String message) {
		message_values.put(key, message);
	}
	
	/**
	 * @param key the String value of the key
	 * @return the String value under the key
	 * */
	public String getMessage(String key) {
		return message_values.get(key).toString();
	}

	/**
	 * @return all the String values in a map
	 * */
	public Map<String, String> messageEntries() {
		return message_values;
	}
}