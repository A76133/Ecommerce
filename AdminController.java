//this contyroller create for admin in which admin dadshboard is open and admin add product.

package in.main.Controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import in.main.Model.Category;
import in.main.Model.Product;
import in.main.Model.ProductOrder;
import in.main.Model.UserDetail;
import in.main.Service.CartService;
import in.main.Service.CategoryService;
import in.main.Service.OrderService;
import in.main.Service.ProductService;
import in.main.Service.userService;
import in.main.util.CommonUtil;
import in.main.util.OrderStatus;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CategoryService Cserv;

	@Autowired
	private ProductService Pserv;
	
	@Autowired
	private userService Userv;
	
	@Autowired
	private CartService Caserv;
	
	@Autowired
	private OrderService Oserv;
	
	@Autowired
	private CommonUtil Cutil;
	
	@Autowired
	private PasswordEncoder Pencode;
	
	
	
	@ModelAttribute
	public void getuserdeatils(Principal p,Model m) {
		if(p!=null) {
			String email=p.getName();
			UserDetail userdtl=Userv.getuserbyemail(email);
			m.addAttribute("user", userdtl);
			Integer countcart=Caserv.getCountCart(userdtl.getId());
			m.addAttribute("countcart",countcart);
			
		}
		
		 List<Category> allcategory=Cserv.getaAllCategory();
    	 m.addAttribute("category", allcategory);
	}

	@GetMapping("/")
	public String index() {
		return "admin/index";
	}

 
	@GetMapping("/category")
	public String category(Model m,@RequestParam(name="pageNo",defaultValue = "0") Integer pageNo ,
			@RequestParam(name="pageSize",defaultValue = "4") Integer pageSize){
		//m.addAttribute("categorys", Cserv.getaAllCategory());
		Page<Category> page=Cserv.getAllCategorypagiation(pageNo, pageSize);
		 List<Category> categorys=page.getContent();
	  	 m.addAttribute("categorys",categorys);
	  	 
	  	 m.addAttribute("pageNo",page.getNumber());
	  	 m.addAttribute("pageSize",pageSize);
	  	 m.addAttribute("totalElements",page.getTotalElements());
	  	 m.addAttribute("totalPages",page.getTotalPages());
	  	 m.addAttribute("isFirst",page.isFirst());
	  	 m.addAttribute("isLast",page.isLast());
	  	 
		return "admin/category";
	}

	@PostMapping("/saveCategory")
	public String savecategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException {

		Boolean exist = Cserv.existcategoryy(category.getName());

		if (exist) {
			session.setAttribute("errorMsg", "Category name already exists");
			return "redirect:/admin/category";
		}

		// Handle Image Upload
		String imageName = "default.jpg";

		if (!file.isEmpty()) {
			imageName = file.getOriginalFilename();

			String uploadDir = "src/main/resources/static/Img/category/";
			File uploadPath = new File(uploadDir);

			if (!uploadPath.exists()) {
				uploadPath.mkdirs(); // create folder if not exist
			}

			Path path = Paths.get(uploadDir + imageName);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		}

		category.setImageName(imageName);

		Category save = Cserv.savecategory(category);

		if (save == null) {
			session.setAttribute("errorMsg", "Not saved! Internal server error");
		} else {
			session.setAttribute("successMsg", "Saved successfully");
		}

		return "redirect:/admin/category";
	}

	@GetMapping("/deletecategory/{id}")
	public String deletecategory(@PathVariable int id, HttpSession session) {
		Boolean delete = Cserv.deletecategory(id);
		if (delete) {
			session.setAttribute("successMsg", "category delete successfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");

		}
		return "redirect:/admin/category";

	}

	@GetMapping("/loadeditcategory/{id}")
	public String loadeditcategory(@PathVariable int id, Model m) {
		m.addAttribute("category", Cserv.getcategorybyid(id));
		return "admin/edit_category";
	}

	@PostMapping("/updateCategory")
	public String updatecategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException {

		Category oldcategory = Cserv.getcategorybyid(category.getId());

		String imagename = oldcategory.getImageName();
		if (!file.isEmpty()) {

			File saveDir = new ClassPathResource("static/Img/category").getFile();

			Path path = Paths.get(saveDir.getAbsolutePath() + File.separator + imagename);

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			oldcategory.setImageName(imagename);
		}

		oldcategory.setName(category.getName());
		oldcategory.setIsActive(category.getIsActive());
		oldcategory.setImageName(imagename);

		Category updatecategory = Cserv.savecategory(oldcategory);

		if (updatecategory != null) {
			session.setAttribute("successMsg", "Category update success");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/admin/loadeditcategory/" + category.getId();
	}

	// ---------------add product------------

	@GetMapping("/loadaddproduct")
	public String add_product(Model m) {
		List<Category> category = Cserv.getaAllCategory();
		m.addAttribute("categaries", category);
		return "admin/add_product";
	}

	@PostMapping("/saveproduct")
	public String saveprduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
			HttpSession session) throws IOException {

		String imageName = "default.jpg";

		product.setDiscount(0);
		product.setDiscountPrice(product.getPrice());

		if (!image.isEmpty()) {

			imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
			product.setImage(imageName);

			File saveDir = new ClassPathResource("static/Img/productImg").getFile();

			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}

			Path path = Paths.get(saveDir.getAbsolutePath() + File.separator + imageName);

			Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		} else {
			product.setImage(imageName);
		}

		Product saveproduct = Pserv.productsave(product);

		if (saveproduct != null) {
			session.setAttribute("successMsg", "Product saved successfully");
		} else {
			session.setAttribute("errorMsg", "Something went wrong");
		}

		return "redirect:/admin/loadaddproduct";
	}

	@GetMapping("/products")
	public String loadviewproduct(Model m,@RequestParam(defaultValue = "") String ch,@RequestParam(name="pageNo",defaultValue = "0") Integer pageNo ,
			@RequestParam(name="pageSize",defaultValue = "8") Integer pageSize) {
		
//		List<Product> searchpr=null;
//		if(ch!=null && ch.length()>0) {
//			searchpr= Pserv.searchproduct(ch);
//			 
//		 }else {
//			 searchpr=Pserv.getallproduct();
//		 }
//		 	m.addAttribute("products", searchpr);
		
		Page<Product> page=null;
		if(ch!=null && ch.length()>0) {
			page= Pserv.searchproductPagination(pageNo, pageSize, ch);
			 
		 }else {
			 page=Pserv.getallproductPagination(pageNo, pageSize);
		 }
		m.addAttribute("products", page.getContent());
		
	  	 m.addAttribute("pageNo",page.getNumber());
	  	 m.addAttribute("pageSize",pageSize);
	  	 m.addAttribute("totalElements",page.getTotalElements());
	  	 m.addAttribute("totalPages",page.getTotalPages());
	  	 m.addAttribute("isFirst",page.isFirst());
	  	 m.addAttribute("isLast",page.isLast());
	  	 
		
		return "admin/products";
	}

	@GetMapping("/deleteproducts/{id}")
	public String deleteproduct(@PathVariable int id, HttpSession session) {
		Boolean deleteproduct = Pserv.delteproduct(id);

		if (deleteproduct) {
			session.setAttribute("successMsg", "product delete successfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");

		}
		return "redirect:/admin/products";
	}

	@GetMapping("/loadeditproduct/{id}")
	public String loadeditproduct(@PathVariable int id, Model m) {
		m.addAttribute("product", Pserv.getproductybyid(id));
		m.addAttribute("categaries", Cserv.getaAllCategory());
		return "admin/edit_product";
	}

	@PostMapping("/updateproducts")
	public String updateproducts(@ModelAttribute Product product, @RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException {

		Product oldproduct = Pserv.getproductybyid(product.getId());

		String imagename = oldproduct.getImage();

		if (!file.isEmpty()) {

			imagename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

			File saveDir = new ClassPathResource("static/Img/productImg").getFile();

			Path path = Paths.get(saveDir.getAbsolutePath() + File.separator + imagename);

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			oldproduct.setImage(imagename);
		}

		oldproduct.setTitle(product.getTitle());
		oldproduct.setDescription(product.getDescription());
		oldproduct.setCategory(product.getCategory());
		oldproduct.setPrice(product.getPrice());
		oldproduct.setStock(product.getStock());
		oldproduct.setIsActive(product.getIsActive());
		oldproduct.setDiscount(product.getDiscount());
		// 5=100*(5/100); 100-5=95

		Double discount = product.getPrice() * (product.getDiscount() / 100.0);
		Double disprice = product.getPrice() - discount;

		oldproduct.setDiscountPrice(disprice);

		if (product.getDiscount() < 0 || product.getDiscount() > 100) {
			session.setAttribute("errorMsg", "invalid discount");

		} else {

		Product updateproduct = Pserv.productsave(oldproduct);

		if (updateproduct != null) {
			session.setAttribute("successMsg", "product update success");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}
		}

		return "redirect:/admin/loadeditproduct/" + product.getId();
	}
	
	@GetMapping("/users")
	public String getAllUsers(Model m,@RequestParam Integer type) {
		List<UserDetail> user=null;
		if(type==1) {
			
			user=Userv.getusers("ROLE_USER");
		}else {
			user=Userv.getusers("ROLE_ADMIN");
			
		}
		m.addAttribute("userType",type);
		m.addAttribute("users", user);
		return "admin/users";
	}
	
	@GetMapping("/updateSts")
	public String updateuseraccountstatus(@RequestParam Boolean status,@RequestParam
			 Integer type,@RequestParam Integer id,HttpSession session) {
		
	    Boolean	f=Userv.updateAccountStatus(id,status);
	    if(f) {
	    	session.setAttribute("successMsg", "Account status udated");
	    }else {
	    	session.setAttribute("errorMsg", "something wrong server");
	    }
		return "redirect:/admin/users?type="+type;
	}
	
	@GetMapping("/orders")
	public String getAllorders(Model m,@RequestParam(name="pageNo",defaultValue = "0") Integer pageNo ,
			@RequestParam(name="pageSize",defaultValue = "8") Integer pageSize) {
	//	List<ProductOrder> allorder=Oserv.getalloredr();
	//	m.addAttribute("orders",allorder);
	//	m.addAttribute("srch",false);
		
		Page<ProductOrder> page=Oserv.getalloredrpagiination(pageNo, pageSize);
		m.addAttribute("orders",page.getContent());
		m.addAttribute("srch",false);
		
		
	  	 m.addAttribute("pageNo",page.getNumber());
	  	 m.addAttribute("pageSize",pageSize);
	  	 m.addAttribute("totalElements",page.getTotalElements());
	  	 m.addAttribute("totalPages",page.getTotalPages());
	  	 m.addAttribute("isFirst",page.isFirst());
	  	 m.addAttribute("isLast",page.isLast());
	  	 
		
		return "admin/orders";
	}
	
	@PostMapping("/update-order-status")
	public String UpdateOrderstatus(
	        @RequestParam Integer id,
	        @RequestParam Integer st,
	        HttpSession session) {

	    try {

	        OrderStatus[] value = OrderStatus.values();
	        String status = null;

	        for (OrderStatus orderst : value) {
	            if (orderst.getId().equals(st)) {
	                status = orderst.getName();
	                break;
	            }
	        }

	        if (status == null) {
	            session.setAttribute("errorMsg", "Please select valid status");
	            return "redirect:/admin/orders";
	        }

	        ProductOrder updateorder = Oserv.updateOrderstatus(id, status);

	        if (!ObjectUtils.isEmpty(updateorder)) {

	            try {
	                Cutil.SendMailForProductUser(updateorder, status);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }

	            session.setAttribute("successMsg", "Status Updated Successfully");

	        } else {
	            session.setAttribute("errorMsg", "Status not updated");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        session.setAttribute("errorMsg", "Something went wrong");
	    }

	    return "redirect:/admin/orders";
	}
	
	
	 @GetMapping("/search-order")
     public String searchProduct(@RequestParam String orderId ,Model m,HttpSession session,@RequestParam(name="pageNo",defaultValue = "0") Integer pageNo ,
 			@RequestParam(name="pageSize",defaultValue = "1") Integer pageSize) {
    	   
        if(orderId != null && orderId.length()>0) {

     	   ProductOrder order=Oserv.getOrderByorderId(orderId.trim());
     	   if(ObjectUtils.isEmpty(order)) {
     		   session.setAttribute("errorMsg", "Incorrect orderId");
     		   m.addAttribute("or",null);
     	   }else {
     		   m.addAttribute("or",order);
     		   
     	   }
     	  m.addAttribute("srch",true);
        }
     	   else {
     		   
     		 // List<ProductOrder> allorder=Oserv.getalloredr();
     		//	m.addAttribute("orders",allorder);
     		//	m.addAttribute("srch",false);
     			
     		  
      		  Page<ProductOrder> page=Oserv.getalloredrpagiination(pageNo, pageSize);
      			m.addAttribute("orders",page.getContent());
      			m.addAttribute("srch",false);
      			
      			m.addAttribute("pageNo",page.getNumber());
      		  	 m.addAttribute("pageSize",pageSize);
      		  	 m.addAttribute("totalElements",page.getTotalElements());
      		  	 m.addAttribute("totalPages",page.getTotalPages());
      		  	 m.addAttribute("isFirst",page.isFirst());
      		  	 m.addAttribute("isLast",page.isLast());
      		  	 
     	   }
     	   
     	  return "/admin/orders";
        }
	
    	 
    	   
    	 
     
	
	 
     @GetMapping("/search")
     public String searchProduct(@RequestParam String ch ,Model m) {
    	 
    	 List<Product> search=Pserv.searchproduct(ch);
    	 m.addAttribute("products",search);
    	
    	   
    	   
    	 return "/admin/products";
     }
         
     @GetMapping("/add-admin")
     public String LoadAdminAdd() {
    	 
    	 
    	 return "/admin/add_admin";
     }
     
     @PostMapping("/save-admin")
     public String saveAdmin(@ModelAttribute UserDetail user,
             @RequestParam("img") MultipartFile file,
             HttpSession session) throws IOException {

         String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
         user.setProfile_image(imageName);

         UserDetail saveuser = Userv.saveAdmin(user);

         if(saveuser != null) {

             if(!file.isEmpty()) {

                 String uploadDir = System.getProperty("user.dir")
                         + "/src/main/resources/static/Img/Saved/";

                 File folder = new File(uploadDir);

                 if(!folder.exists()){
                     folder.mkdirs();
                 }

                 Path path = Paths.get(uploadDir + imageName);

                 Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                 System.out.println("Image saved at: " + path);
             }

             session.setAttribute("successMsg", "Register successfully");

         } else {
             session.setAttribute("errorMsg", "Not saved! Internal server error");
         }

         return "redirect:/admin/add-admin";
     }
     
     @PostMapping("/update-profile")
 	public String userprofile(@ModelAttribute UserDetail userdtl,@RequestParam MultipartFile img,HttpSession session) {
 		UserDetail update=Userv.updateuserprofile(userdtl, img);
 		if(ObjectUtils.isEmpty(update)) {
 			session.setAttribute("errorMsg", "Profile not updated");
 			
 		}else {
 			session.setAttribute("successMsg","Profile Updated");
 		
 		}
 		return "redirect:/admin/profile";
 	}
     @GetMapping("/profile")
 	public String profile() {
 		return "/admin/profile";
 	}
     
     private UserDetail getLoggedInUserDetails(Principal p) {

 		String email=p.getName();
 		UserDetail userdtl=Userv.getuserbyemail(email);
 		
 		return userdtl;
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
 		return "redirect:/admin/profile";
 	}
 	
 	
     
	
	

}
