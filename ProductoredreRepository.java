package in.main.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.main.Model.ProductOrder;

public interface ProductoredreRepository extends JpaRepository<ProductOrder, Integer>{

	List<ProductOrder> findByUserId(Integer userId);

	ProductOrder findByOrderId(String orderId);

}
