package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
public class UserService {
	
	public static final int USERS_PER_PAGE = 4;
	
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User getByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}
	
	public List<User> listAll() {
		return (List<User>) userRepo.findAll();
	}
	
	public Page<User> listPage(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum-1, USERS_PER_PAGE);
		return userRepo.findAll(pageable);
		//List<User> listPage = page.getContent();
		// return listPage(4);
		
	}
	
	public Page<User> listPage(int pageNum,String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum-1, USERS_PER_PAGE, sort);
		
		if(keyword != null && !keyword.isEmpty() ) {
			return userRepo.findAll(keyword, pageable);
		}
			return userRepo.findAll(pageable);
		
		
		//List<User> listPage = page.getContent();
		// return listPage(4);
		
	}
	
	public List<Role> listRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public User save(User user) {
		
		boolean isUpdatingUser = (user.getId() != null);
		
		System.out.println("Update user22");
		
		if(isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();
			
			if(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
			
		} else {
			System.out.println("Last passgenerate");
			encodePassword(user);
		}
		
		return userRepo.save(user);
	}
	
	public User updateAccount(User userInform) {
		System.out.println("Update work!!!");
		User userInDB = userRepo.findById(userInform.getId()).get();
		
		System.out.println("in service user in db=="+userInDB);
		
		if(!userInform.getPassword().isEmpty()) {
			userInDB.setPassword(userInform.getPassword());
			encodePassword(userInDB);
		}
		
		if(userInform.getPhotos() != null) {
			userInDB.setPhotos(userInform.getPhotos());
		}
		userInDB.setFirstName(userInform.getFirstName());
		userInDB.setLastName(userInform.getLastName());
		
		return userRepo.save(userInDB);
		
	}
	
	public void encodePassword(User user) {
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
	}
	
	public boolean isEmailUnique(String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		System.out.println("userByEmail======"+userByEmail);
		return userByEmail == null;
	}
	
	public void deleteById(Integer id) {
		
		Long countId = userRepo.countById(id);
		if(countId == null || countId == 0) {
			throw new UsernameNotFoundException("Could not find any user with id "+id);
		}
		
		userRepo.deleteById(id);
		//User deleteUser = new User();
		
		
	}

	public User get(Integer id) throws UserNotFoundException {
		
		try {
		return userRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user");
		}
	}

	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepo.countById(id);
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user "+ id);
		}
		userRepo.deleteById(id);
		
	}
	
	public void updateUserStatus(Integer id, boolean status) throws UserNotFoundException {
		Long countById = userRepo.countById(id);
		if(countById == null || countById == 0 ) {
			throw new UserNotFoundException("Could not find any user "+ id);
		}
		
		userRepo.updateEnableStatus(id, status);
	}
	
	//public User get(Integer id) throws usernot

}
