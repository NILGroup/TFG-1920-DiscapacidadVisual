package com.avanti.test;

import java.io.IOException;

import junit.framework.TestCase;

import com.avanti.http.HttpServices;

public class TestHttp extends TestCase {
	
	private HttpServices http;
	
	public TestHttp(String s){
		super(s);
		http = new HttpServices();
	}

	public void testLogin() throws IOException{
		String nombre = "Fran";
		String pass = "fran";
		String sol = http.login(nombre, pass);
		assertEquals("",sol);
	}
	
	public void testLogout() {
		
	}

	public void testRegister() {
		fail("Not yet implemented");
	}

}
