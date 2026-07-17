package in.main.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import in.main.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
  
	
	 List<Product> findByIsActiveTrue();
	 

	 List<Product> findByCategory(String category);
	 
	 List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch,String ch2);

	 Page<Product> findByIsActiveTrue(Pageable pageble);

	 Page<Product> findByCategory(Pageable pageble,String category);

	 Page<Product>  findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2, Pageable pageble);


	Page<Product> findByIsActiveTrueAndTitleContainingIgnoreCaseOrIsActiveTrueAndCategoryContainingIgnoreCase(String ch,
			String ch2, Pageable pageable);
}