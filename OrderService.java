package in.main.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import in.main.Model.OrderRequest;
import in.main.Model.ProductOrder;

public interface OrderService {
    public void saveOrder(Integer userId,OrderRequest orderRequest) throws Exception;
    
    public List<ProductOrder> getOrderByUser(Integer userId);
    
    public ProductOrder updateOrderstatus(Integer id,String status);
    
    public List<ProductOrder> getalloredr();
    
    public ProductOrder getOrderByorderId(String orderId);
    
    public Page<ProductOrder> getalloredrpagiination(Integer pageNo,Integer pageSize);
}
