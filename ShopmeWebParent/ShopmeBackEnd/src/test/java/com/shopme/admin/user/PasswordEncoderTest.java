package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	
	@Test
	public void testEncodePassword() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "channa2020";
		String encodePassword = bCryptPasswordEncoder.encode(rawPassword);
		System.out.println(encodePassword);
		
		boolean matche = bCryptPasswordEncoder.matches(rawPassword, encodePassword);
		
		assertThat(matche).isTrue();
		if(matche) {
			System.out.println("Password Maches");
		} else {
			System.out.println("Password not Matches");
		}
		
	}

}
