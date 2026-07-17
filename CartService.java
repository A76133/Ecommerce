package in.main.Service;

import java.util.List;

import in.main.Model.Cart;

public interface CartService {

	
	public Cart saveCart(Integer productId,Integer userid);
	
	public List<Cart> getCartsByUser(Integer userId);
	
	public Integer getCountCart(Integer userId);

	public void updateQuantiity(String sy, Integer cid);
	
}
