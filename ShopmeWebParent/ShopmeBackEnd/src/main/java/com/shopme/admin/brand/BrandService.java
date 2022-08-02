package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;

@Service
@Transactional
public class BrandService {
	
	public static final int ROOT_BRANDS_PER_PAGE = 10;
	
	
	@Autowired
	private BrandRepository repo;
	
	public  List<Brand> listAll() {
		return (List<Brand>) repo.findAll();
	}
	
	public Page<Brand> listByPage(
			int pageNum
			, String sortField
			, String sortDir
			, String keyword
			) {
		
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
//		if (sortDir.equals("asc")) {
//			 sort = sort.ascending();
//		 } else if (sortDir.equals("desc"))  {
//			 sort = sort.descending();
//		 }
		
		Pageable pageable = PageRequest.of(pageNum - 1, ROOT_BRANDS_PER_PAGE, sort);
		
		if(keyword != null ) {
			
			return repo.findAll(keyword, pageable);
		}
		
		
		return repo.findAll(pageable);
		
	}
	
	public Brand get(Integer id) throws BrandNotFoundException {
		
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new BrandNotFoundException("Cound not find any brand with id "+ id);
		}
		
	}
	
	public void deleteByid(Integer id) throws BrandNotFoundException {
		Long countId = repo.countById(id);
		
		if(countId == null || countId == 0 ) {
			throw new BrandNotFoundException("Cound not find any brand with id "+id);
		}
		
		repo.deleteById(id);
	}
	
	public Brand save(Brand brand) {
		return repo.save(brand);
	}

	public String checkNameUnique(Integer id, String name) {
		
		boolean isCreatingNew  = (id == null || id == 0);
		
		
		Brand brandByName = repo.findByName(name);
		
		
		if (isCreatingNew) {
			if(brandByName != null ) {
				return "Duplicate";
			}		
		}
		else {
			if(brandByName != null && brandByName.getId() != id) {
				return "Duplicate";
			}
		}
		return "OK";
	}
	
	
	
	

}
