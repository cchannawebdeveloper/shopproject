package com.shopme.admin.model;

public class ResponeResult<T> {
	
	ResponseMessage result; 
	
	T object;
	
	public ResponeResult() {
		// TODO Auto-generated constructor stub
	}
	
	

	public ResponeResult(ResponseMessage result, T object) {
		super();
		this.result = result;
		this.object = object;
	}



	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}
	
	
	
	
	

}
