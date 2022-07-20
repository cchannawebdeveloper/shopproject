package com.shopme.admin;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;

import com.shopme.admin.category.CategoryService;

public class Main {
	
	@Autowired
	static
	CategoryService service;
	
	
	public static void main(String[] args) {
		
		Person p11 = new Person("channa", 31);
		Person p12 = new Person("pheary", 32);
		Person p3 = new Person("lin", 22);
		
		ArrayList<Person> namePerson = new ArrayList<Person>();
		
		namePerson.add(p11);
		namePerson.add(p12);
		namePerson.add(p3);
		
		System.out.println("name1=="+namePerson.get(0).name);
		System.out.println("name1=="+namePerson.get(1).name);
		System.out.println("channa age=="+namePerson.get(0).age);
		
		System.out.println("namePerson===="+namePerson);
		
		
		
		ArrayList<String> names = new ArrayList<String>();
		names.add("channa");
		names.add("linda");
		names.add("Thida");
		Map<String, Object> map = new HashedMap<>();
		map.put("id", 1);
		map.put("name", "channa");
		
		for (Map.Entry<String, Object> m : map.entrySet()) 
			System.out.println("key =="+m.getKey() +" and value =="+m.getValue());
		
		//Pen p = new Pen();
		//System.out.println(p.clicked);
		//p.clicked();
		//System.out.println(p.clicked);
		//System.out.println(p.cl);
		//System.out.print(p.unClick());
		//System.out.println(p.color + " " + p.type );
		//p.unClicked();
		//System.out.println(p.clicked);
		
		Pen pen = new Pen();
		pen.name = "channa22";
		pen.level = 20;
		
		
		System.out.println("Pen 2= "+pen.name);
		Integer id = 3;
		
		boolean isCreatingNew = (id == null || id == 0);
		
		if(isCreatingNew) {
			System.out.println("id=== null or id === 0");
		} else {
			System.out.println("id has value!!!");
		}
		
		
		
		//String result = service.maskAccountNumber("12343");
		
		//System.out.println("Result=="+result);
		
		
		
		
	}
	
	
}

class Person {
	String name;
	int age;
	public Person() {
		// TODO Auto-generated constructor stub
	}
	public Person(String name, int age) {
		//super();
		this.name = name;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
