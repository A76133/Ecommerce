package in.main.Service;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;

import in.main.Model.Category;

public interface CategoryService {
   
	public Category savecategory(Category category);
	
	public Boolean existcategoryy(String name);
	
	public List<Category> getaAllCategory();
	
	public Boolean deletecategory(int id);
	
	public Category getcategorybyid(int id);
	
	public List<Category> gatAllActivecategory();
	
	public Page<Category> getAllCategorypagiation(Integer pageNo,Integer pageSize);
}
