package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.SocketUtils;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {
	
	@Autowired
	private BrandRepository repo;
	
	@Test
	public void testCreateBrand() {
		
		Category laptops = new Category(6);
		//System.out.println("laptops=="+laptops);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(laptops);
		
		Brand saveBrand = repo.save(acer);
		assertThat(saveBrand).isNotNull();
		assertThat(saveBrand.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testCreateBrand2() {
		Category cellphone = new Category(4);
		Category tablets = new Category(7);
		
		Brand apple = new Brand("apple", "apple.png");
		apple.getCategories().add(cellphone);
		apple.getCategories().add(tablets);
		
		Brand savedBrand = repo.save(apple);
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand3() {
		Category memory = new Category(29);
		Category hardDrive = new Category(24);
		
		Brand samsung = new Brand("samsung", "samsung.png");
		
		samsung.getCategories().add(memory);
		samsung.getCategories().add(hardDrive);
		
		Brand savedBrand = repo.save(samsung);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAll() {
		Iterable<Brand> list = repo.findAll();
		
		list.forEach(System.out::println);
		assertThat(list).isNotNull();
	}
	
	@Test
	public void testFindById() {
		Brand brandById = repo.findById(1).get();
		
		assertThat(brandById.getName()).isEqualTo("Acer");
	}
	
	@Test
	public void testUpdateName() {
		String newName = "Acer Electronic";
		
		Brand acer =  repo.findById(1).get();
		acer.setName(newName);
		
		Brand save = repo.save(acer);
		assertThat(save.getName()).isEqualTo(newName);
		
	}
	
	@Test
	public void testEqual() {
		//assertThat("123",is("123"));
	}
	
	@Test
	public void testDeleteById() {
		Integer id = 12;
		repo.deleteById(id);
		
		Optional<Brand> result1 = repo.findById(id);
		//Brand result = repo.findById(id);
		
		assertThat(result1.isEmpty());
	}
	
	

}
