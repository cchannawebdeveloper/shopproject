package com.shopme.admin;

public class Headphones {
	String charge = "Micro usb";
	String[] controls = {"channa","rithy"};
	String color = "Red/Black";
	
	static boolean power = false;
	static int valume = 0;
	
	public static void powerOn() {
		power = true;
		
	}
	public static void powerOff() {
		power = false;
	}
	
	public static void valumeUp() {
		valume++;
	}
	public static void valumeDown() {
		valume--;
	}

}
