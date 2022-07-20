package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {
	
	@Autowired
	private CategoryRepository repo;
	
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Food");
		Category saveCategory =  repo.save(category);
		
		assertThat(saveCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(7);
		
		Category memory = new Category("Memory", parent);
		//Category smartphones = new Category("Smartphones", parent);
		//Category saveSubCat = repo.save(subCategory);
		
		//repo.saveAll(List.of(cameras,smartphones));
		Category saveSubCategory = repo.save(memory);
		
		assertThat(saveSubCategory.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testGetCategory() {
		Category category = repo.findById(2).get();
		
		System.out.println("Get Catogory=="+category.getName());
		System.out.println("Get img=="+category.getImage());
		//System.out.println("Get children=="+category.getChildren());
		
		Set<Category> children = category.getChildren();
		
		for (Category categoryLoop : children ) {
			System.out.println("children category get name==="+categoryLoop.getName());
		}
		// children = category.getChildren();
		
		assertThat(children.size()).isGreaterThan(0);
	}
	
	@Test
	public void testPrintHierarchicalCategories() {
		//Iterable<Category> categoies = repo.findAll();
		
		ArrayList<Category> categoies = (ArrayList<Category>) repo.findAll();
		
		//System.out.println("category1=="+categoies);
		
		for( Category category : categoies) {
			if(category.getParent() == null ) {
				
				System.out.println(category.getName());
				
				Set<Category> children = category.getChildren();
				
				for ( Category subCategory : children ) {
					System.out.println("--"+subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
			
		}
		
	// category =	repo.findAll();
	}
	
	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		//System.out.println("parent=="+parent.getChildren().getClass().getSimpleName());
		
		Set<Category> childrens = parent.getChildren();
		
		for (Category subCategory : childrens ) {
			for (int i=0;i<newSubLevel;i++) {
				System.out.print("--");
			}
			
			System.out.println(subCategory.getName());
			printChildren(subCategory, newSubLevel);
		}
		
		
	}
	
	@Test
	public void testListRootCategory() {
	 //List<Category> rootCategories = 	repo.findRootCategories(null);
	 //rootCategories.forEach(cat -> System.out.println(cat.getName()));
	}
	
	@Test
	public void testFindCategoryName() {
		String name = "Computer";
		Category saveCategory = repo.findByName(name);
		
		assertThat(saveCategory).isNotNull();
		assertThat(saveCategory.getName()).isEqualTo(name);
		
		//assertThat(saveCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAlias() {

		String alias = "Computer";
		Category findAlias = repo.findByAlias(alias);
		
		
		
		assertThat(findAlias).isNotNull();
		assertThat(findAlias.getAlias()).isEqualTo(alias);
		
	
	}
	
	
	

}
