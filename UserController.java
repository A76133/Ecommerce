package in.main.Controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import in.main.Model.Cart;
import in.main.Model.Category;
import in.main.Model.OrderRequest;
import in.main.Model.ProductOrder;
import in.main.Model.UserDetail;
import in.main.Service.CartService;
import in.main.Service.CategoryService;
import in.main.Service.OrderService;
import in.main.Service.userService;
import in.main.util.CommonUtil;
import in.main.util.OrderStatus;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {
   
	
	@Autowired
	private userService Userv;
	
	@Autowired
	private CategoryService Cserv;
     
	@Autowired
	private CartService Caserv;
	
	@Autowired
	private OrderService Oserv;
	
	@Autowired
	private CommonUtil Cutil;
	
	@Autowired
	private PasswordEncoder Pencode;
	
	
	@GetMapping("/")
	public String home() {
		return "user/home";
		
	}
	
	
	@ModelAttribute
	public void getuserdeatils(Principal p,Model m) {
		if(p!=null) {
			String email=p.getName();
			UserDetail userdtl=Userv.getuserbyemail(email);
			m.addAttribute("user", userdtl);
			
		}
		
		 List<Category> allcategory=Cserv.getaAllCategory();
    	 m.addAttribute("category", allcategory);
	}

	@GetMapping("/addcart")
	public String addToCart(
	        @RequestParam Integer pid,
	        @RequestParam Integer uid,
	        HttpSession session) {

	    Cart savecart = Caserv.saveCart(pid, uid);

	    if (ObjectUtils.isEmpty(savecart)) {
	        session.setAttribute("errorMsg",
	                "product add to cart failed");
	    } else {
	        session.setAttribute("successMsg",
	                "product add to cart");
	    }

	    return "redirect:/view/" + pid;
	}
	
	@GetMapping("/cart")
	public String loadCartPage(Principal p,Model m) {
		
		UserDetail user=getLoggedInUserDetails(p);
				List<Cart> cart=Caserv.getCartsByUser(user.getId());
				m.addAttribute("cart",cart);
				if(cart.size()>0) {
				Double gettotalorder=cart.get(cart.size()-1).getTotalOrderPrice();
				m.addAttribute("totalOrderPrice",gettotalorder);
				}
				return "/user/Cart";
	}
	
	
	
	@GetMapping("/cartQuantityUpdate")
	public String updateCartQuantity(@RequestParam String sy,@RequestParam Integer cid) {
		
		Caserv.updateQuantiity(sy,cid);
		return "redirect:/user/cart";
	}


	private UserDetail getLoggedInUserDetails(Principal p) {

		String email=p.getName();
		UserDetail userdtl=Userv.getuserbyemail(email);
		
		return userdtl;
	}
	
	@GetMapping("/orders")
	public String orderPage(Principal p,Model m){

		UserDetail user=getLoggedInUserDetails(p);
				List<Cart> cart=Caserv.getCartsByUser(user.getId());
				m.addAttribute("cart",cart);
				if(cart.size()>0) {
				Double orderprice=cart.get(cart.size()-1).getTotalOrderPrice();
				Double gettotalorder=cart.get(cart.size()-1).getTotalOrderPrice() + 250 + 100;
				m.addAttribute("orderprice",orderprice);
				m.addAttribute("totalOrderPrice",gettotalorder);
				}
		return "/user/order";
	}
	
	@PostMapping("/save-order")
	public String saveprder(@ModelAttribute OrderRequest request,Principal p) throws Exception{
		UserDetail user=getLoggedInUserDetails(p);
		Oserv.saveOrder(user.getId(), request);
		return "redirect:/user/success";
	}
	
	@GetMapping("/success")
	public String loadSuccess() {
		
		return "/user/success";
		
	}
	

	@GetMapping("/user-orders")
	public String myOrders(Model m,Principal p) {
		UserDetail users=getLoggedInUserDetails(p);
		List<ProductOrder> orders=Oserv.getOrderByUser(users.getId());
		m.addAttribute("orders",orders);
		return "/user/my_orders";
		
	}
	
	@GetMapping("/update-status")
	public String UpdateOrderstatus(@RequestParam Integer id, @RequestParam Integer st,HttpSession session) throws UnsupportedEncodingException, MessagingException {
		OrderStatus[] value=OrderStatus.values();
		String status=null;
		for(OrderStatus orderst:value) {
			if(orderst.getId().equals(st)) {
				status=orderst.getName();
			}
		}
		ProductOrder updateorder=Oserv.updateOrderstatus(id, status);
		Cutil.SendMailForProductUser(updateorder, status);
		
		if(!ObjectUtils.isEmpty(updateorder)) {
			session.setAttribute("successMsg","Status Updated");
		}else {
			session.setAttribute("errorMsg", "Status not updated");
		}
		
		return "redirect:/user/user-orders";
		
	}
	
	@GetMapping("/profile")
	public String profile() {
		return "/user/profile";
	}
	
	@PostMapping("/update-profile")
	public String userprofile(@ModelAttribute UserDetail userdtl,@RequestParam MultipartFile img,HttpSession session) {
		UserDetail update=Userv.updateuserprofile(userdtl, img);
		if(ObjectUtils.isEmpty(update)) {
			session.setAttribute("errorMsg", "Profile not updated");
			
		}else {
			session.setAttribute("successMsg","Profile Updated");
		
		}
		return "redirect:/user/profile";
	}
	
	@PostMapping("/change-password")
	public String changepassword(@RequestParam String newPassword,@RequestParam String currentpassword,Principal p,HttpSession session) {
		 
		UserDetail getlog=getLoggedInUserDetails(p);
		boolean match=Pencode.matches(currentpassword, getlog.getPassword());
		if(match) {
			String encode=Pencode.encode(newPassword);
			 getlog.setPassword(encode);
			 UserDetail update=Userv.updateuser(getlog);
			 if(ObjectUtils.isEmpty(update)) {
				 session.setAttribute("errorMsg", "Password is not updated || error in server");
				
			}else {
				session.setAttribute("successMsg","password Successfully Updated");
			
			}
		}else {
			session.setAttribute("errorMsg", "current password Incorrect");
		}
		return "redirect:/user/profile";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
