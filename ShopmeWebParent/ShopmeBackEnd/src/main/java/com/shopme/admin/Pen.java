package com.shopme.admin;

public class Pen {
	
	String name;
	int level;
	
	
	
	public Pen(String name, int level) {
		super();
		this.name = name;
		this.level = level;
	}

	String type = "gel";
	String color = "blue";
	int point = 10;
	
	static boolean clicked = false;
	
	public static void clicked() {
		clicked = true;
		
	}
	
	public static void unClicked() {
		clicked = false;
	}
	
	public Pen() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Pen Class [type=" + type + ", color=" + color + ", point=" + point + "]";
	}
	
	

}
