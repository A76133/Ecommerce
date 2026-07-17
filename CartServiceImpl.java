package in.main.Service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import in.main.Model.Cart;
import in.main.Model.Product;
import in.main.Model.UserDetail;
import in.main.Repository.CartRepository;
import in.main.Repository.ProductRepository;
import in.main.Repository.UserRepository;
import in.main.Service.CartService;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
	private CartRepository Crepo;
    @Autowired
    private UserRepository Urepo;
    @Autowired
    private ProductRepository Prepo;
    
	
	
	@Override
	public Cart saveCart(Integer productId, Integer userid) {
		UserDetail userdtl=Urepo.findById(userid).get();
		Product product=Prepo.findById(productId).get();
		
		Cart cartStatus=Crepo.findByProductIdAndUserId(productId, userid);
		Cart cart=null;
		if(ObjectUtils.isEmpty(cartStatus)) {
			cart=new Cart();
			cart.setProduct(product);
			cart.setUser(userdtl);
			cart.setQuantity(1);
			cart.setTotalprice(1*product.getDiscountPrice());
			
		}else {
			cart=cartStatus;
			cart.setQuantity(cart.getQuantity()+1);
			cart.setTotalprice(cart.getQuantity()*cart.getProduct().getDiscountPrice());
		}
		
		Cart savecart=Crepo.save(cart);
		
		return savecart;
	}

	@Override
	public List<Cart> getCartsByUser(Integer userId) {
		List<Cart> cart=Crepo.findByUserId(userId);
		Double totalOrderPrice=0.0;
		
		List<Cart> cartlist=new ArrayList<>();
		
		for(Cart c:cart) {
			
		   Double totalPrice=(c.getProduct().getDiscountPrice() * c.getQuantity());
			c.setTotalprice(totalPrice);
			totalOrderPrice =totalOrderPrice + totalPrice;
			c.setTotalOrderPrice(totalOrderPrice);
			cartlist.add(c);
		}
		
		
		
		return cartlist;
	}

	@Override
	public Integer getCountCart(Integer userId) {
		Integer count=Crepo.countByUserId(userId);
		return count;
	}

	@Override
	public void updateQuantiity(String sy, Integer cid) {
		Cart cart=Crepo.findById(cid).get();
		int updateQuantity;
		
		if(sy.equalsIgnoreCase("de")) {
			updateQuantity=cart.getQuantity() - 1;
			
			if(updateQuantity<=0) {
				Crepo.delete(cart);
				
			}else {
				cart.setQuantity(updateQuantity);
				Crepo.save(cart);
			}
		}else {
		    updateQuantity=cart.getQuantity() + 1;	
		    cart.setQuantity(updateQuantity);
			Crepo.save(cart);
		}
		
	
	}
	

}