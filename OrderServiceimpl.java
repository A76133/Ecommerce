package in.main.Service.impl;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.main.Model.Cart;
import in.main.Model.OrderAddress;
import in.main.Model.OrderRequest;
import in.main.Model.ProductOrder;
import in.main.Repository.CartRepository;
import in.main.Repository.ProductoredreRepository;
import in.main.Service.OrderService;
import in.main.util.CommonUtil;
import in.main.util.OrderStatus;
import jakarta.mail.MessagingException;
@Service
public class OrderServiceimpl implements OrderService {
    @Autowired
	private ProductoredreRepository Orepo;
    
    @Autowired
    private CartRepository Crepo;
    
    @Autowired
	private CommonUtil Cutil;
	
	
	
	@Override
	public void saveOrder(Integer userId,OrderRequest orderRequest) throws UnsupportedEncodingException, MessagingException {
		
		List<Cart> cart=Crepo.findByUserId(userId);
		
		
		for(Cart ca:cart) {
			
			ProductOrder order=new ProductOrder();
			
			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(LocalDate.now());
			order.setProduct(ca.getProduct());
			order.setPrice(ca.getProduct().getDiscountPrice());
			order.setQuantity(ca.getQuantity());
			order.setUser(ca.getUser());
			order.setStatus(OrderStatus.IN_PROGRESS.getName()
					);
			order.setPaymentType(orderRequest.getPaymentType());
			
			
			OrderAddress address=new OrderAddress();
			
			address.setFirstName(orderRequest.getFirstName());
			address.setLastName(orderRequest.getLastName());
			address.setEmail(orderRequest.getEmail());
			address.setMobileNo(orderRequest.getMobileNo());
			address.setAddress(orderRequest.getAddress());
			address.setCity(orderRequest.getCity());
			address.setState(orderRequest.getState());
			address.setPincode(orderRequest.getPincode());
			

			order.setOrderAddress(address);
			
			ProductOrder save=Orepo.save(order);
			Cutil.SendMailForProductUser(save, "success");
			
		}
		
		
	}

	@Override
	public List<ProductOrder> getOrderByUser(Integer userId) {
		 List<ProductOrder> orders=Orepo.findByUserId(userId);
		return orders;
	}

	@Override
	public ProductOrder updateOrderstatus(Integer id, String status) {
	 Optional<ProductOrder> find=Orepo.findById(id);
	 if(find.isPresent()) {
		 ProductOrder pid=find.get();
		 pid.setStatus(status);
		 ProductOrder updateorder=Orepo.save(pid);
		 return updateorder;
	 }
		return null;
	}

	@Override
	public List<ProductOrder> getalloredr() {
		return Orepo.findAll();
		
	}
	
	@Override
	public Page<ProductOrder> getalloredrpagiination(Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		Pageable pageble=PageRequest.of(pageNo, pageSize);
				
		return Orepo.findAll(pageble);
	}

	@Override
	public ProductOrder getOrderByorderId(String orderId) {
		return Orepo.findByOrderId(orderId);
		
	}

	

}
