package in.main.Service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import in.main.Model.Product;
import in.main.Repository.ProductRepository;
import in.main.Service.ProductService;
@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
	private ProductRepository Prepo;
	
	@Override
	public Product productsave(Product product) {
		
		return Prepo.save(product);
		
		
	}

	@Override
	public List<Product> getallproduct() {
		
		return Prepo.findAll();
	}
  
	@Override
	public Page<Product> getallproductPagination(Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		Pageable pageble=PageRequest.of(pageNo, pageSize);
		
		return  Prepo.findAll(pageble);
	}
	
	@Override
	public Boolean delteproduct(int id) {
		Product product=Prepo.findById(id).orElse(null);
		if(!ObjectUtils.isEmpty(product)) {
			Prepo.delete(product);
			return true;
		}
		return false;
	}

	@Override
	public Product getproductybyid(Integer id) {
		Product product=Prepo.findById(id).orElse(null);
		return product;
	}

	@Override
	public List<Product> getallActiveproduct(String category) {
		// TODO Auto-generated method stub
		
		List<Product> product=null;
		if(ObjectUtils.isEmpty(category)) {
			product=Prepo.findByIsActiveTrue();
		}else {
			
			product=Prepo.findByCategory(category);
		}
		
		return product;
	}

	@Override
	public List<Product> searchproduct(String ch) {
		return Prepo.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
		
	}
	

	@Override
	public Page<Product> searchproductPagination(Integer pageNo, Integer pageSize, String ch) {
	   Pageable pageble=PageRequest.of(pageNo, pageSize);
	   return Prepo.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch,pageble);
		
		
	}

	@Override
	public Page<Product> getAllActiveProducts(Integer pageNo, Integer pageSize,String category) {
		
		Pageable pageble=PageRequest.of(pageNo, pageSize);
		Page<Product> pageproduct=null;
		
		if(ObjectUtils.isEmpty(category)) {
			pageproduct=Prepo.findByIsActiveTrue(pageble);
		}else {
			
			pageproduct=Prepo.findByCategory(pageble,category);
		}
		
		
		
		return pageproduct;
	}

	@Override
	public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String category, String ch) {

	    Pageable pageable = PageRequest.of(pageNo, pageSize);

	    return Prepo
	        .findByIsActiveTrueAndTitleContainingIgnoreCaseOrIsActiveTrueAndCategoryContainingIgnoreCase(ch, ch, pageable);
	}
	
	
	}

	



