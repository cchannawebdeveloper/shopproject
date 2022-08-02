package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void testCreateProduct() {
		Brand brand = testEntityManager.find(Brand.class, 10);
		
		Category category = testEntityManager.find(Category.class, 15);
		
		Product product = new Product();
		product.setName("Samsung Galaxy A31126");
		product.setAlias("alias_samsung6");
		product.setShortDescription("A good smartphone from Samsung");
		product.setFullDescription("This a good smartphone from Samsung Full Description ");
		product.setBrand(brand);
		product.setCategory(category);
		product.setPrice(356);
		product.setInStock(true);
		product.setEnabled(true);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		
		Product saveProduct = repo.save(product);
		
		assertThat(saveProduct).isNotNull();
		assertThat(saveProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProduct = repo.findAll();
		
		iterableProduct.forEach(System.out::println);
	}
	
	@Test
	public void testGetProduct() {
		Integer id = 1;
		Product findById = repo.findById(id).get();
		System.out.println("Hello33"+findById);
		assertThat(findById).isNotNull();
		//assertThat(findById.getName()).isEqualTo("Samsung Galaxy A31122");
		//findById
	}
	
	
	@Test
	public void testUpdateProduct() {
		String productName = "Samsung Galaxy A312";
		Integer id = 1;
		Product findById = repo.findById(id).get();
		findById.setName(productName);
		
		Product updateProduct = repo.save(findById);
		assertThat(updateProduct.getName()).isEqualTo(productName);
	}
	
	@Test
	public void testUpdatePriceProduct() {
		Integer id = 1;
		Product updateProduct = repo.findById(id).get();
		updateProduct.setPrice(379);
		
		repo.save(updateProduct);
		
		Product findProduct = testEntityManager.find(Product.class, id);
		
		System.out.println("Product id 1="+findProduct);
		
		assertThat(findProduct.getPrice()).isEqualTo(379);
	}
	
	@Test
	public void testDeleteProduct() {
		Integer id = 7;
		repo.deleteById(id);
		
		Optional<Product> findProduct = repo.findById(id);
		
		//assertThat(findProduct.isEmpty());
		
		assertThat(!findProduct.isPresent());
		
	}
	
	@Test
	public void testSaveProductWithImages() {
		Integer id = 5;
		Product findProduct = repo.findById(id).get();
		
		//findProduct.setMainImage("main image3.jpg");
		findProduct.addExtraImage("extra image_12.jpg");
		findProduct.addExtraImage("extra image_22.jpg");
		//findProduct.addExtraImage("extra image_3.jpg");
		//findProduct.addExtraImage("extra image_4.jpg");
		
		Product saveProduct = repo.save(findProduct);
		
		assertThat(saveProduct.getImages().size()).isEqualTo(2);
		
	}
	
	

}
