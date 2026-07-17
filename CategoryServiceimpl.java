package in.main.Service.impl;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import in.main.Model.Category;
import in.main.Repository.CategoryRepository;
import in.main.Service.CategoryService;

@Service
public class CategoryServiceimpl implements CategoryService{
     
	@Autowired
	private CategoryRepository Crepo;
	 
	@Override
	public Category savecategory(Category category) {
		
		return Crepo.save(category);
	}
	
	
	
	@Override
	public List<Category> getaAllCategory() {
		
		return Crepo.findAll();
	}



	@Override
	public Boolean existcategoryy(String name) {
		// TODO Auto-generated method stub
		return Crepo.existsByName(name);
	}



	@Override
	public Boolean deletecategory(int id) {
	      Category category=Crepo.findById(id).orElse(null);
	      if(!ObjectUtils.isEmpty(category)) {
	    	  Crepo.delete(category);
	    	  return true;
	      }
		return false;
	}



	@Override
	public Category getcategorybyid(int id) {
		// TODO Auto-generated method stub
		
		Category category=Crepo.findById(id).orElse(null);
		
		return category;
	}



	@Override
	public List<Category> gatAllActivecategory() {
		// TODO Auto-generated method stub
		
		List<Category> categories=Crepo.findByIsActiveTrue();
		return categories;
	}






	@Override
	public Page<Category> getAllCategorypagiation(Integer pageNo, Integer pageSize) {

	    org.springframework.data.domain.Pageable pageable =
	            org.springframework.data.domain.PageRequest.of(pageNo, pageSize);

	    return Crepo.findAll(pageable);
		   
	}

	
}
