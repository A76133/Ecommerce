package in.main.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import in.main.Model.Product;

public interface ProductService {
  
	public Product productsave(Product product);
	
	public List<Product> getallproduct();
	
	public Boolean delteproduct(int id);
	
	public Product getproductybyid(Integer id);
	
	public List<Product> getallActiveproduct(String category);
	
	public List<Product> searchproduct(String ch);
	
	public Page<Product> getAllActiveProducts(Integer pageNo,Integer pageSize,String category);

	public Page<Product> searchproductPagination(Integer pageNo,Integer pageSize, String ch);
	
	public Page<Product> getallproductPagination(Integer pageNo,Integer pageSize);

	public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String category, String ch);
	
	
	
	
	
	
	
	
	
	
}
