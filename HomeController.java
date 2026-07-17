//this home controller for all fornt pages needs to user navigation and in hich add all usr navigation 
//for this in add services and serviceimpl and repository and configuration and entitiy in same project


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
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import in.main.Model.Category;
import in.main.Model.Product;
import in.main.Model.UserDetail;
import in.main.Service.CartService;
import in.main.Service.CategoryService;
import in.main.Service.ProductService;
import in.main.Service.userService;
import in.main.util.CommonUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private CategoryService Cserv;
	
	@Autowired
	private ProductService Pserv;
	
	@Autowired
	private userService Userv;
	
	@Autowired
	private CommonUtil util;
	
	@Autowired
	private BCryptPasswordEncoder pass;
	
	@Autowired
	private CartService Caserv;
	
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
	public String index(Model m) {
	   List<Category> allCategory=Cserv.gatAllActivecategory().stream()
			   .sorted((c1,c2)->c2.getId().compareTo(c1.getId()))
			   .limit(6).toList();
	  List<Product> allproduct= Pserv.getallActiveproduct("").stream()
			  .sorted((p1,p2)->p2.getId().compareTo(p1.getId()))
			  .limit(8).toList();
	   m.addAttribute("category",allCategory);
	   m.addAttribute("products",allproduct);
	   return "index";
   }
     
     @GetMapping("/signin")
 	public String login() {
 	   
 	   return "Login";
    }
     
     @GetMapping("/register")
 	public String register() {
 	   
 	   return "Registration";
    }
      
     
     @GetMapping("/product")
  	public String product(Model m,@RequestParam(value = "category",defaultValue = "") String category
  			,@RequestParam(name = "pageNo",defaultValue = "0") Integer pageNo,
  			@RequestParam(name = "pageSize",defaultValue = "9") Integer pageSize
  			,@RequestParam(defaultValue = "") String ch) {
  	   
    	 List<Category> categories=Cserv.gatAllActivecategory();
  	   m.addAttribute("paramValue", category);
  	   m.addAttribute("categories", categories);
  	   
  	// List<Product> products=Pserv.getallActiveproduct(category);
  	 //  m.addAttribute("products", products);
  	 Page<Product>  page= null;
  	   if(StringUtils.isEmpty(ch)) {
  		page= Pserv.getAllActiveProducts(pageNo, pageSize, category);
  	   }else {
  		   page=Pserv.searchActiveProductPagination(pageNo,pageSize,category,ch);
  	   }
  
  	 List<Product> products=page.getContent();
  	 m.addAttribute("products",products);
  	 m.addAttribute("productsSize",pageSize);
  	 m.addAttribute("pageNo",page.getNumber());
  	 m.addAttribute("pageSize",pageSize);
  	 m.addAttribute("totalElements",page.getTotalElements());
  	 m.addAttribute("totalPages",page.getTotalPages());
  	 m.addAttribute("isFirst",page.isFirst());
  	 m.addAttribute("isLast",page.isLast());
  	 
  	   return "product";
     }
     
     @GetMapping("/view/{id}")
   	public String view_product(@PathVariable int id,Model m) {
   	   Product productbyid=Pserv.getproductybyid(id);
   	   m.addAttribute("product",productbyid);
   	   return "view_product";
      }
     
     @PostMapping("/saveuser")
     public String saveuser(@ModelAttribute UserDetail user,
             @RequestParam("img") MultipartFile file,
             HttpSession session) throws IOException {

    	 
    	Boolean email= Userv.existsEmail(user.getEmail());
    	
    	
    	if(email) {
    		session.setAttribute("errorMsg", "Email already exist");
    		
    	}else {
    		

            String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
            user.setProfile_image(imageName);

            UserDetail saveuser = Userv.saveuser(user);
            
            
           	 
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

    			}
    	 
         return "redirect:/register";
     }
     
     
     //forget password
     @GetMapping("/forget-password")
     public String showforgetpassword() {
    	 return "forget_password.html";
     }

@PostMapping("/forget-password")
public String processforgetpassword(
        @RequestParam String email,
        HttpSession session,
        HttpServletRequest request)
        throws UnsupportedEncodingException, MessagingException {

    UserDetail useremail = Userv.getuserbyemail(email);

    if (ObjectUtils.isEmpty(useremail)) {
        session.setAttribute("errorMsg", "Invalid email");

    } else {

        String resettoken = UUID.randomUUID().toString();

        Userv.updateuserresettoken(email, resettoken);

        // Correct Reset Password URL
        String url = CommonUtil.generateUrl(request)
                + "/reset-password?token=" + resettoken;

        Boolean send = util.sendmail(url, email);

        if (send) {
            session.setAttribute("successMsg",
                    "Please check your email... password reset link sent");
        } else {
            session.setAttribute("errorMsg",
                    "Something wrong in server | Email not sent");
        }
    }

    return "redirect:/forget-password";
}
     
     @GetMapping("/reset-password")
     public String showresetpassword(@RequestParam String token,HttpSession session,Model m) {
    	 
    	 UserDetail userbytoken=Userv.getuserbytoken(token);
    	 
    	 if(ObjectUtils.isEmpty(userbytoken)) {
    		 m.addAttribute("msg", "your link is invalid or expired !!");
    		 return "message";
    	 } 
    	m.addAttribute("token",token);
    	 return "reset_password";
     }
     
     
     @PostMapping("/reset-password")
     public String resetpassword(@RequestParam String token,@RequestParam String password,HttpSession session,Model m) {
    	 
    	 UserDetail userbytoken=Userv.getuserbytoken(token);
    	 if(ObjectUtils.isEmpty(userbytoken)) {
    		 m.addAttribute("msg", "your link is invalid or expired !!");
    		 return "message";
    	 } else {
    		 userbytoken.setPassword(pass.encode(password));
    		 userbytoken.setResetToken(null);
    		 Userv.updateuser(userbytoken);
    		 session.setAttribute("successMsg", "password change successfully");
    		 m.addAttribute("msg","password change successfully");
    		 return "message";
    	 }
    	
     }
     
     @GetMapping("/search")
     public String searchProduct(@RequestParam String ch ,Model m) {
    	 
    	 List<Product> search=Pserv.searchproduct(ch);
    	 m.addAttribute("products",search);
    	 
    	 List<Category> categories=Cserv.gatAllActivecategory();
    	   m.addAttribute("categories", categories);
    	   
    	   
    	   
    	 return "product";
     }
   
}











