package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shopme.admin.user.CategoryNotFoundException;
import com.shopme.common.entity.Category;

@Service
@Transactional
public class CategoryService {
	
	public static final int ROOT_CATEGORIES_PER_PAGE = 4;
	
	@Autowired
	private CategoryRepository repo;
	
	public List<Category> listByPage(
			CategoryPageInfo pageInfo
			, int pageNum
			, String sortDir
			, String keyword) 
	{
		
		Sort sort = Sort.by("name");
		 
		 if (sortDir.equals("asc")) {
			 sort = sort.ascending();
		 } else if (sortDir.equals("desc"))  {
			 sort = sort.descending();
		 }
		 
		 Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sort);
		 
		 Page<Category> pageCategories = null;
		 
		 if(keyword != null && !keyword.isEmpty()) {
			 pageCategories = repo.search(keyword, pageable);
		 } else {
			 pageCategories = repo.findRootCategories(pageable);
		 }
		 
		 
		///return (List<Category>) repo.findAll();
		 List<Category> rootCategories = pageCategories.getContent();
		
		pageInfo.setTotalElements(pageCategories.getTotalElements());
		pageInfo.setTotalPages(pageCategories.getTotalPages());
		
		 if(keyword != null && !keyword.isEmpty()) { 
			 
			 List<Category> searchResult = pageCategories.getContent();
			 for( Category category : searchResult ) {
				 category.setHasChildren(category.getChildren().size() > 0);
			 }
			 return searchResult;
			 
		 }
		 else {
			 return listHierarchicalCategories(rootCategories, sortDir);
		 }
		
		
	}
	
//	public List<Category> listAll(String sortDir) {
//		 Sort sort = Sort.by("name");
//		 
//		 if (sortDir.equals("asc")) {
//			 sort = sort.ascending();
//		 } else if (sortDir.equals("desc"))  {
//			 sort = sort.descending();
//		 }
//		///return (List<Category>) repo.findAll();
//		List<Category> rootCategories = repo.findRootCategories(sort);
//		return  listHierarchicalCategories(rootCategories, sortDir);
//	}
	
	
	private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
		
		List<Category> hierarchicalCategories = new ArrayList<>();
		
		for (Category rootCategory : rootCategories) {
			
			hierarchicalCategories.add(Category.copyFull(rootCategory));
			
			
			Set<Category> children = sortSubCategories( rootCategory.getChildren(), sortDir );
			
			for (Category subCategory : children) {
				String name = "--"+subCategory.getName();
				
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
			}
		}
		
		return hierarchicalCategories;
	}
	
	private void listSubHierarchicalCategories(
			List<Category> hierarchicalCategories
			, Category parent
			, int subLevel
			, String sortDir) {
		
		int newSubLevel = subLevel + 1;
		Set<Category> children = sortSubCategories( parent.getChildren(), sortDir);
		
		for(Category subCategory : children) {
			
			String name = "";
			for (int i=0;i<newSubLevel;i++) {
				name += "--";
			}
			
			name += subCategory.getName();
			
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
			
		}
		
	}
	
	public Category save(Category category) {
		return repo.save(category);	
	}
	
	public List<Category> listCategoriesUseInForm() {
		
		List<Category> categoriesUseInForm = new ArrayList<>();
		
		Iterable<Category> categoriesInDB = repo.findAll();
		
		//ArrayList<Category> categoriesInDB = (ArrayList<Category>) repo.findAll();
		
		for( Category category : categoriesInDB) {
			
			if(category.getParent() == null ) {
				
				categoriesUseInForm.add(Category.copyIDAndName(category));
				
				//categoriesUseInDB.add(new ca);
				
				Set<Category> children = sortSubCategories ( category.getChildren());
				
				for ( Category subCategory : children ) {
					String name = "--"+subCategory.getName();
					categoriesUseInForm.add(Category.copyIDAndName(subCategory.getId(), name));
					listChildren(categoriesUseInForm, subCategory, 1);
				}
			}
		}
		
		return categoriesUseInForm;
	}
	
	private void listChildren(List<Category> categoriesUseInForm, Category parent, int subLevel) {
		
		int newSubLevel = subLevel + 1;
		
		Set<Category> childrens = parent.getChildren();
		
		for (Category subCategory : childrens ) {
			String name = "";
			for (int i=0;i<newSubLevel;i++) {
				System.out.print("--");
				name += "--";
			}
			
			name += subCategory.getName();
			
			categoriesUseInForm.add(Category.copyIDAndName(subCategory.getId(), name));
			listChildren(categoriesUseInForm, subCategory, newSubLevel);
			
		}
	}


	public Category get(Integer id) throws CategoryNotFoundException {
		
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find any category");
		}
	}
	
	public String checkUnique(Integer id, String name, String alias) {
		System.out.println("id=="+id);
		boolean isCreatingNew  = (id == null || id == 0);
		Category categoryByName = repo.findByName(name);
		System.out.println("CategoryByName=="+categoryByName);
		
		if (isCreatingNew) {
			if(categoryByName != null ) {
				return "DuplicateName";
			} else {
				Category categoryByAlias = repo.findByAlias(alias);
				if (categoryByAlias != null ) {
					return "DuplicateAlias";
				}
			}
			
		}
		else {
			if(categoryByName != null && categoryByName.getId() != id) {
				return "DuplicateName";
			}
			
			Category categoryByAlias = repo.findByAlias(alias);
			
			if(categoryByAlias != null && categoryByAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}
		return "OK";
	}
	
	private SortedSet<Category> sortSubCategories( Set<Category> children) {
		
		//SortedSet<Category>
		
		
		return sortSubCategories(children, "asc");
	}
	
	
	private SortedSet<Category> sortSubCategories( Set<Category> children, String sortDir) {
		
		SortedSet<Category> sortedChildren = new TreeSet<>( new Comparator<Category>() {
			//sortDir = "asc";
			@Override
			public int compare(Category cat1, Category cat2) {
				if(sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());
				} else if(sortDir.equals("desc")) {
					return cat2.getName().compareTo(cat1.getName());
				} else {
					return 1;
				}
				
			}
		});
		sortedChildren.addAll(children);
		return sortedChildren;
		
	}


	public void updateCategoriesStatus(
			  Integer id
			, boolean status
			) throws CategoryNotFoundException {
		Long countById = repo.countById(id);
		System.out.println("countById=="+countById);
		
		if(countById == null || countById == 0 ) {
			throw new CategoryNotFoundException("Could not find any category "+id);
			
		}
		//System.out.println();
		
		repo.updateEnableCategoryStatus(id, status);
		
		//repo
		//System.out.println("coundById=="+countById);
		
	}


	public void delete(Integer id) throws CategoryNotFoundException {
		Long countById = repo.countById(id);
		System.out.println("coundId=="+countById);
		
		if(countById == null || countById == 0 ) {
			throw new CategoryNotFoundException("Could not find any category "+id);
			
		}
		
		repo.deleteById(id);
		
	}
	
	@SuppressWarnings("deprecation")
	public String maskAccountNumber(String number) {
		if(StringUtils.isEmpty(number)) {
			return "";
		} 
		else if (number.length() != 5) {
			return number;
		}
		else {
			return number +"xxxConvert";
		}
	}
	

	

}
