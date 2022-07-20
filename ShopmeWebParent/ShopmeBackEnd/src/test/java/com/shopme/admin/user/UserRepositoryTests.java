package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	//@Autowired
	//private TestEntityManager entityManager;
	
//	@Test
//	public void createNewUserTestwithOneRole() {
//		Role roleAdmin = entityManager.find(Role.class, 1);
//		Map<String, Object> results = new HashMap<String, Object>();
//		//results.put("k", "value");
//		
//		//TestEntityManager entityManager;
//		
//		User createUser = new User("channa", "yoeurn", ""
//				+ "cchannayoeurng@gmail.com", "channa17");
//		
//		
//		
//		createUser.addRole(roleAdmin);
//		User userSave = repo.save(createUser);
//		assertThat(userSave.getId()).isGreaterThan(0);
//	}
	
	@Test
	public void createNewUserTestwithTwoRole() {
		//Role roleEditor = entityManager.find(Role.class, 4);
		
		User thirdUser = new User("pheary", "Ro", "pheary@gmail.com", "phear###");
		
		Role roleEditor = new Role(4);
		Role roleAss = new Role(6);
		
		
		thirdUser.addRole(roleEditor);
		thirdUser.addRole(roleAss);
		
		User saveUser = repo.save(thirdUser);
		
		assertThat(saveUser.getId()).isGreaterThan(0);
		
		//secondUser.addRole(roleEditor);
		
		//User userSave = repo.save(secondUser);
		
	//	assertThat(userSave.getId()).isGreaterThan(0);
		
		
		
	}
	
	@Test
	public void testListAllUser() {
		Iterable<User> listAllUsers = repo.findAll();
		listAllUsers.forEach(user -> System.out.println(user));
		
	}
	
	@Test
	public void testGetUserById(Integer id) {
		User findUser = repo.findById(1).get();
		System.out.println("findUser = "+findUser);
		assertThat(findUser).isNotNull();
		
	}
	
	@Test
	public void testUpdateUserDetails() {
		User findUser = repo.findById(1).get();
		findUser.setEnabled(true);
		findUser.setEmail("cchannaupdate@gmail2.com");
		findUser.setFirstName("channaUpdate");
		findUser.setLastName("yoeurnUpdate");
		
		repo.save(findUser);
		
		//assertThat(updateData);
	}
	
	@Test
	public void testUpdateRoleUser() {
		
		User findUser = repo.findById(3).get();
		Role roleEditor = new Role(4);
		Role roleSalePerson = new Role(3);
		
		findUser.getRoles().remove(roleEditor);
		findUser.addRole(roleSalePerson);
		
		repo.save(findUser);
		
	}
	
	@Test
	public void testDeleteUser() {
		//User findUser = repo.findById(3).get();
		Integer usrId = 2;
		repo.deleteById(usrId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "newmailwithinput@gmail.com";
		User user = repo.getUserByEmail(email);
		assertThat(user).isNotNull();
		System.out.println("User in tes: "+user);
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		long countId = repo.countById(id);
		assertThat(countId).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDeleteUser1() {
		Integer id = 3;
		repo.deleteById(id);
		///return null;
		
	}
	
	@Test
	public void testUserDisable() {
		Integer userId = 39;
		repo.updateEnableStatus(userId, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		org.springframework.data.domain.Pageable pageable = 
				PageRequest.of(pageNumber, pageSize);
		
		Page<User> page = repo.findAll(pageable);
		List<User> userList = page.getContent();
		userList.forEach(user -> System.out.println(user));
		assertThat(userList.size()).isEqualTo(pageSize);
		
	}
	
	@Test
	public void testSearchUser() {
		String keyword = "afaf";
		
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);
		
		List<User> lisUsers = page.getContent();
		lisUsers.forEach(user -> System.out.println(user));
		System.out.println("work");
		
		assertThat(lisUsers.size()).isGreaterThan(0);
	}
	
	
	
	

}
